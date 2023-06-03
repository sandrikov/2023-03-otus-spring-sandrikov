package ru.otus.homework.books.services;

import lombok.val;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.homework.books.domain.Genre;
import ru.otus.homework.books.dto.GenreDto;
import ru.otus.homework.books.mappers.GenreMapper;
import ru.otus.homework.books.repository.BookRepository;
import ru.otus.homework.books.repository.GenreRepository;
import ru.otus.homework.books.services.misc.EntityNotFoundException;
import ru.otus.homework.books.services.misc.ServiceUtils;

import java.util.List;
import java.util.Objects;

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

    private final BookRepository bookRepository;

    private final GenreMapper genreMapper;

    public GenreServiceImpl(GenreRepository genreRepository, BookRepository bookRepository, GenreMapper genreMapper) {
        this.genreRepository = genreRepository;
        this.bookRepository = bookRepository;
        this.genreMapper = genreMapper;
    }

    @Transactional(readOnly = true)
    @Override
    public ServiceResponse<List<GenreDto>> listGenres() {
        return done(genreRepository.findAll().stream().map(genreMapper::toDto).toList());
    }

    @Transactional(readOnly = true)
    @Override
    public ServiceResponse<GenreDto> getGenre(long id) {
        try {
            return done(genreMapper.toDto(findGenre(id)));
        } catch (EntityNotFoundException e) {
            return error(e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    @Override
    public ServiceResponse<GenreDto> findGenre(String name) {
        return genreRepository.findByName(name)
                .map(genreMapper::toDto).map(ServiceResponse::done)
                .orElseGet(() -> error(String.format(GENRE_NOT_FOUND, name)));
    }

    @Transactional
    @Override
    public ServiceResponse<GenreDto> createGenre(String name) {
        if (genreRepository.findByName(name).isPresent()) {
            return error(String.format(GENRE_ALREADY_EXISTS, name));
        }
        val genre = new Genre(name);
        try {
            genreRepository.save(genre);
            return done(genreMapper.toDto(genre));
        } catch (DuplicateKeyException e) {
            return error(String.format(GENRE_ALREADY_EXISTS, name));
        }
    }

    @Transactional
    @Override
    public ServiceResponse<GenreDto> renameGenre(long id, String name) {
        Genre genre;
        try {
            genre = findGenre(id);
        } catch (EntityNotFoundException e) {
            return error(e.getMessage());
        }
        if (!Objects.equals(name, genre.getName())) {
            if (genreRepository.findByName(name).isPresent()) {
                return error(String.format(GENRE_ALREADY_EXISTS, name));
            }
            genre.setName(name);
            genreRepository.save(genre);
        }
        return done(genreMapper.toDto(genre));
    }

    @Transactional
    @Override
    public ServiceResponse<GenreDto> deleteGenre(long id) {
        Genre genre;
        try {
            genre = findGenre(id);
        } catch (EntityNotFoundException e) {
            return error(e.getMessage());
        }
        if (bookRepository.countByAuthorAndGenreAndTitle(null, genre, null) > 0) {
            return error(String.format(INTEGRITY_VIOLATION_ERROR, id));
        }
        genreRepository.delete(genre);
        return done(genreMapper.toDto(genre));
    }

    private Genre findGenre(Long genreId) throws EntityNotFoundException {
        return ServiceUtils.findById(genreId, genreRepository::findById, GENRE_NOT_FOUND);
    }
}
