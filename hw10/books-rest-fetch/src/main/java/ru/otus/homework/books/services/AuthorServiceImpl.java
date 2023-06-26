package ru.otus.homework.books.services;

import lombok.val;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.homework.books.domain.Author;
import ru.otus.homework.books.mappers.AuthorMapper;
import ru.otus.homework.books.repository.AuthorRepository;
import ru.otus.homework.books.rest.dto.AuthorDto;
import ru.otus.homework.books.services.misc.EntityNotFoundException;
import ru.otus.homework.books.services.misc.Reply;
import ru.otus.homework.books.services.misc.ServiceUtils;

import java.util.List;
import java.util.Objects;

import static ru.otus.homework.books.services.misc.Reply.done;
import static ru.otus.homework.books.services.misc.Reply.error;
import static ru.otus.homework.books.services.misc.ServiceErrorMessages.AUTHOR_NOT_FOUND;
import static ru.otus.homework.books.services.misc.ServiceErrorMessages.getAuthorAlreadyExistsMessage;
import static ru.otus.homework.books.services.misc.ServiceErrorMessages.getAuthorNotFoundMessage;

@Service
public class AuthorServiceImpl implements AuthorService {

    public static final String INTEGRITY_VIOLATION_ERROR = """
            Deletion is not possible: there are author's books.
            First call: delete books --author-id %s
            """;

    private final AuthorRepository authorRepository;

    private final AuthorMapper authorMapper;

    private final BookService bookService;

    public AuthorServiceImpl(AuthorRepository authorRepository, AuthorMapper authorMapper,
                             @Lazy BookService bookService) {
        this.authorRepository = authorRepository;
        this.bookService = bookService;
        this.authorMapper = authorMapper;
    }

    @Override
    public Reply<List<AuthorDto>> listAuthors() {
        val authors = authorRepository.findAll().stream().map(authorMapper::toDto).toList();
        return done(authors);
    }

    @Override
    public Reply<AuthorDto> getAuthor(long id) {
        try {
            return done(authorMapper.toDto(findAuthor(id)));
        } catch (EntityNotFoundException e) {
            return error(e.getMessage());
        }
    }

    @Override
    public Reply<AuthorDto> findAuthor(String name) {
        return authorRepository.findByName(name)
                .map(authorMapper::toDto).map(Reply::done)
                .orElseGet(() -> error(getAuthorNotFoundMessage(name)));
    }

    @Transactional
    @Override
    public Reply<AuthorDto> createAuthor(String name) {
        if (authorRepository.findByName(name).isPresent()) {
            return error(getAuthorAlreadyExistsMessage(name));
        }
        var author = new Author(name);
        authorRepository.save(author);
        return done(authorMapper.toDto(author));
    }

    @Transactional
    @Override
    public Reply<AuthorDto> renameAuthor(long id, String name) {
        Author author;
        try {
            author = findAuthor(id);
        } catch (EntityNotFoundException e) {
            return error(e.getMessage());
        }
        if (!Objects.equals(name, author.getName()) &&
                authorRepository.findByName(name).isPresent()) {
            return error(getAuthorAlreadyExistsMessage(name));
        }
        author.setName(name);
        authorRepository.save(author);
        return done(authorMapper.toDto(author));
    }


    @Transactional
    @Override
    public Reply<AuthorDto> deleteAuthor(long id) {
        if (bookService.existsByAuthorId(id)) {
            return error(String.format(INTEGRITY_VIOLATION_ERROR, id));
        }
        try {
            val author = findAuthor(id);
            authorRepository.delete(author);
            return done(authorMapper.toDto(author));
        } catch (EntityNotFoundException e) {
            return error(e.getMessage());
        }
    }

    @Override
    public Author findAuthor(Long genreId) {
        return ServiceUtils.findById(genreId, authorRepository::findById, AUTHOR_NOT_FOUND);
    }

}
