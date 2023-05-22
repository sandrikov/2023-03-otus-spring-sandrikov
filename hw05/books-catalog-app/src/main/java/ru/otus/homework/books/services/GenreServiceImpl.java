package ru.otus.homework.books.services;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.otus.homework.books.dao.GenreRepository;
import ru.otus.homework.books.model.Genre;

import java.util.List;

import static ru.otus.homework.books.services.ServiceResponse.done;
import static ru.otus.homework.books.services.ServiceResponse.error;

@Service
public class GenreServiceImpl implements GenreService {

    public static final String GENRE_NOT_FOUND = "Genre is not found: %s";

    public static final String GENRE_ALREADY_EXISTS = "Genre already exists: %s";

    public static final String INTEGRITY_VIOLATION_ERROR = """
            Deletion is not possible: there are genre's books.
            First call: delete-books --genre-id %s
            """;

    private final GenreRepository genreRepository;

    public GenreServiceImpl(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Override
    public ServiceResponse<List<Genre>> listGenres() {
        return done(genreRepository.findAll());
    }

    @Override
    public ServiceResponse<Genre> getGenre(long id) {
        try {
            return done(genreRepository.findById(id));
        } catch (EmptyResultDataAccessException e) {
            return error(String.format(GENRE_NOT_FOUND, "id=" + id));
        }
    }

    @Override
    public ServiceResponse<Genre> findGenre(String name) {
        var result = genreRepository.findByName(name);
        return result.map(ServiceResponse::done)
                .orElseGet(() -> error(String.format(GENRE_NOT_FOUND, name)));
    }

    @Override
    public ServiceResponse<Genre> createGenre(String name) {
        var genre = new Genre(name);
        try {
            genreRepository.save(genre);
            return done(genre);
        } catch (DuplicateKeyException e) {
            return error(String.format(GENRE_ALREADY_EXISTS, name));
        }
    }

    @Override
    public ServiceResponse<Genre> renameGenre(long id, String name) {
        Genre genre;
        try {
            genre = genreRepository.findById(id);
        } catch (EmptyResultDataAccessException e) {
            return error(String.format(GENRE_NOT_FOUND, "id=" + id));
        }
        genre.setName(name);
        try {
            genreRepository.save(genre);
            return done(genre);
        } catch (DuplicateKeyException e) {
            return error(String.format(GENRE_ALREADY_EXISTS, name));
        }
    }

    @Override
    public ServiceResponse<Genre> deleteGenre(long id) {
        Genre genre;
        try {
            genre = genreRepository.findById(id);
        } catch (EmptyResultDataAccessException e) {
            return error(String.format(GENRE_NOT_FOUND, "id=" + id));
        }
        try {
            genreRepository.delete(genre);
            return done(genre);
        } catch (DataIntegrityViolationException e) {
            return error(String.format(INTEGRITY_VIOLATION_ERROR, id));
        }
    }

}
