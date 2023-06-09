package ru.otus.homework.books.services;

import lombok.val;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.homework.books.domain.Genre;
import ru.otus.homework.books.mappers.GenreMapper;
import ru.otus.homework.books.repository.GenreRepository;
import ru.otus.homework.books.rest.dto.GenreDto;
import ru.otus.homework.books.services.misc.EntityNotFoundException;
import ru.otus.homework.books.services.misc.Reply;
import ru.otus.homework.books.services.misc.ServiceUtils;

import java.util.List;
import java.util.Objects;

import static ru.otus.homework.books.services.misc.Reply.done;
import static ru.otus.homework.books.services.misc.Reply.error;
import static ru.otus.homework.books.services.misc.ServiceErrorMessages.GENRE_NOT_FOUND;
import static ru.otus.homework.books.services.misc.ServiceErrorMessages.getGenreAlreadyExistsMessage;
import static ru.otus.homework.books.services.misc.ServiceErrorMessages.getGenreNotFoundMessage;

@Service
public class GenreServiceImpl implements GenreService {

    public static final String INTEGRITY_VIOLATION_ERROR = """
            Deletion is not possible: there are genre's books.
            First call: delete-books --genre-id %s
            """;

    private final GenreRepository genreRepository;

    private final GenreMapper genreMapper;

    private final BookService bookService;

    public GenreServiceImpl(GenreRepository genreRepository, GenreMapper genreMapper,
                            @Lazy BookService bookService) {
        this.genreRepository = genreRepository;
        this.bookService = bookService;
        this.genreMapper = genreMapper;
    }

    @Override
    public Reply<List<GenreDto>> listGenres() {
        return done(genreRepository.findAll().stream().map(genreMapper::toDto).toList());
    }

    @Override
    public Reply<GenreDto> getGenre(long id) {
        try {
            return done(genreMapper.toDto(findGenre(id)));
        } catch (EntityNotFoundException e) {
            return error(e.getMessage());
        }
    }

    @Override
    public Reply<GenreDto> findGenre(String name) {
        return genreRepository.findByName(name)
                .map(genreMapper::toDto).map(Reply::done)
                .orElseGet(() -> error(getGenreNotFoundMessage(name)));
    }

    @Transactional
    @Override
    public Reply<GenreDto> createGenre(String name) {
        if (genreRepository.findByName(name).isPresent()) {
            return error(getGenreAlreadyExistsMessage(name));
        }
        val genre = new Genre(name);
        genreRepository.save(genre);
        return done(genreMapper.toDto(genre));
    }

    @Transactional
    @Override
    public Reply<GenreDto> renameGenre(long id, String name) {
        Genre genre;
        try {
            genre = findGenre(id);
        } catch (EntityNotFoundException e) {
            return error(e.getMessage());
        }
        if (!Objects.equals(name, genre.getName()) && genreRepository.findByName(name).isPresent()) {
            return error(getGenreAlreadyExistsMessage(name));
        }
        genre.setName(name);
        genreRepository.save(genre);
        return done(genreMapper.toDto(genre));
    }

    @Transactional
    @Override
    public Reply<GenreDto> deleteGenre(long id) {
        if (bookService.existsByGenreId(id)) {
            return error(String.format(INTEGRITY_VIOLATION_ERROR, id));
        }
        try {
            val genre = findGenre(id);
            genreRepository.delete(genre);
            return done(genreMapper.toDto(genre));
        } catch (EntityNotFoundException e) {
            return error(e.getMessage());
        }
    }

    @Override
    public Genre findGenre(Long genreId) {
        return ServiceUtils.findById(genreId, genreRepository::findById, GENRE_NOT_FOUND);
    }
}
