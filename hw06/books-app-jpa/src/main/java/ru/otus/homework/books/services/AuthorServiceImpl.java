package ru.otus.homework.books.services;

import lombok.val;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.homework.books.domain.Author;
import ru.otus.homework.books.dto.AuthorDto;
import ru.otus.homework.books.mappers.AuthorMapper;
import ru.otus.homework.books.repository.AuthorRepository;
import ru.otus.homework.books.repository.BookRepository;
import ru.otus.homework.books.services.misc.EntityNotFoundException;
import ru.otus.homework.books.services.misc.ServiceUtils;

import java.util.List;
import java.util.Objects;

import static ru.otus.homework.books.services.ServiceResponse.done;
import static ru.otus.homework.books.services.ServiceResponse.error;

@Service
public class AuthorServiceImpl implements AuthorService {

    public static final String AUTHOR_NOT_FOUND = "Author is not found: %s";

    public static final String AUTHOR_ALREADY_EXISTS = "Author already exists: %s";

    public static final String INTEGRITY_VIOLATION_ERROR = """
            Deletion is not possible: there are author's books.
            First call: delete books --author-id %s
            """;

    private final AuthorRepository authorRepository;

    private final BookRepository bookRepository;

    private final AuthorMapper authorMapper;

    public AuthorServiceImpl(AuthorRepository authorRepository, BookRepository bookRepository,
                             AuthorMapper authorMapper) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
        this.authorMapper = authorMapper;
    }

    @Transactional(readOnly = true)
    @Override
    public ServiceResponse<List<AuthorDto>> listAuthors() {
        val authors = authorRepository.findAll().stream().map(authorMapper::toDto).toList();
        return done(authors);
    }

    @Transactional(readOnly = true)
    @Override
    public  ServiceResponse<AuthorDto> getAuthor(long id) {
        try {
            return done(authorMapper.toDto(findAuthor(id)));
        } catch (EntityNotFoundException e) {
            return error(e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    @Override
    public ServiceResponse<AuthorDto> findAuthor(String name) {
        return authorRepository.findByName(name)
                .map(authorMapper::toDto).map(ServiceResponse::done)
                .orElseGet(() -> error(String.format(AUTHOR_NOT_FOUND, name)));
    }

    @Transactional
    @Override
    public ServiceResponse<AuthorDto> createAuthor(String name) {
        if (authorRepository.findByName(name).isPresent()) {
            return error(String.format(AUTHOR_ALREADY_EXISTS, name));
        }
        var author = new Author(name);
        try {
            authorRepository.save(author);
            return done(authorMapper.toDto(author));
        } catch (DuplicateKeyException e) {
            return error(String.format(AUTHOR_ALREADY_EXISTS, name));
        }
    }

    @Transactional
    @Override
    public ServiceResponse<AuthorDto> renameAuthor(long id, String name) {
        Author author;
        try {
            author = findAuthor(id);
        } catch (EntityNotFoundException e) {
            return error(e.getMessage());
        }
        if (!Objects.equals(name, author.getName())) {
            if (authorRepository.findByName(name).isPresent()) {
                return error(String.format(AUTHOR_ALREADY_EXISTS, name));
            }
            author.setName(name);
            authorRepository.save(author);
        }
        return done(authorMapper.toDto(author));
    }


    @Transactional
    @Override
    public ServiceResponse<AuthorDto> deleteAuthor(long id) {
        Author author;
        try {
            author = findAuthor(id);
        } catch (EntityNotFoundException e) {
            return error(e.getMessage());
        }
        if (bookRepository.countByAuthorAndGenreAndTitle(author, null, null) > 0) {
            return error(String.format(INTEGRITY_VIOLATION_ERROR, id));
        }
        authorRepository.delete(author);
        return done(authorMapper.toDto(author));
    }

    private Author findAuthor(Long genreId) throws EntityNotFoundException {
        return ServiceUtils.findById(genreId, authorRepository::findById, AUTHOR_NOT_FOUND);
    }

}
