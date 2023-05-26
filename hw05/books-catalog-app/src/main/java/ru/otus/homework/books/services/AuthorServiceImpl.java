package ru.otus.homework.books.services;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.otus.homework.books.dao.AuthorRepository;
import ru.otus.homework.books.model.Author;

import java.util.List;

import static ru.otus.homework.books.services.ServiceResponse.done;
import static ru.otus.homework.books.services.ServiceResponse.error;

@Service
public class AuthorServiceImpl implements AuthorService {

    public static final String AUTHOR_NOT_FOUND = "Author is not found: %s";

    public static final String AUTHOR_ALREADY_EXISTS = "Author already exists: %s";

    public static final String INTEGRITY_VIOLATION_ERROR = """
            Deletion is not possible: there are author's books.
            First call: delete-books --author-id %s
            """;

    private final AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public ServiceResponse<List<Author>> listAuthors() {
        return done(authorRepository.findAll());
    }

    @Override
    public  ServiceResponse<Author> getAuthor(long id) {
        try {
            return done(authorRepository.findById(id));
        } catch (EmptyResultDataAccessException e) {
            return error(String.format(AUTHOR_NOT_FOUND, "id=" + id));
        }
    }

    @Override
    public ServiceResponse<Author> findAuthor(String name) {
        var res = authorRepository.findByName(name);
        return res.map(ServiceResponse::done)
                .orElseGet(() -> error(String.format(AUTHOR_NOT_FOUND, name)));
    }

    @Override
    public ServiceResponse<Author> createAuthor(String name) {
        var author = new Author(name);
        try {
            authorRepository.save(author);
            return done(author);
        } catch (DuplicateKeyException e) {
            return error(String.format(AUTHOR_ALREADY_EXISTS, name));
        }
    }

    @Override
    public ServiceResponse<Author> renameAuthor(long id, String name) {
        Author author;
        try {
            author = authorRepository.findById(id);
        } catch (EmptyResultDataAccessException e) {
            return error(String.format(AUTHOR_NOT_FOUND, "id=" + id));
        }
        author.setName(name);
        try {
            authorRepository.save(author);
            return done(author);
        } catch (DuplicateKeyException e) {
            return error(String.format(AUTHOR_ALREADY_EXISTS, name));
        }
    }

    @Override
    public ServiceResponse<Author> deleteAuthor(long id) {
        Author author;
        try {
            author = authorRepository.findById(id);
        } catch (EmptyResultDataAccessException e) {
            return error(String.format(AUTHOR_NOT_FOUND, "id=" + id));
        }
        try {
            authorRepository.delete(author);
            return done(author);
        } catch (DataIntegrityViolationException e) {
            return error(String.format(INTEGRITY_VIOLATION_ERROR, id));
        }
    }


}
