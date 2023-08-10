package ru.otus.homework.butterflygarden .services;

import lombok.val;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.homework.butterflygarden .domain.Genre;
import ru.otus.homework.butterflygarden .dto.GenreDto;
import ru.otus.homework.butterflygarden .mappers.GenreMapper;
import ru.otus.homework.butterflygarden .repository.GenreRepository;
import ru.otus.homework.butterflygarden .services.misc.EntityNotFoundException;
import ru.otus.homework.butterflygarden .services.misc.ServiceUtils;

import java.util.List;
import java.util.Objects;

import static ru.otus.homework.butterflygarden .services.ServiceResponse.done;
import static ru.otus.homework.butterflygarden .services.ServiceResponse.error;

@Service
public class GenreServiceImpl implements GenreService {

    public static final String GENRE_NOT_FOUND = "Genre is not found: %s";

    public static final String GENRE_ALREADY_EXISTS = "Genre already exists: %s";

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
    public ServiceResponse<List<GenreDto>> listGenres() {
        return done(genreRepository.findAll().stream().map(genreMapper::toDto).toList());
    }

    @Override
    public ServiceResponse<GenreDto> getGenre(long id) {
        try {
            return done(genreMapper.toDto(findGenre(id)));
        } catch (EntityNotFoundException e) {
            return error(e.getMessage());
        }
    }

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
        genreRepository.save(genre);
        return done(genreMapper.toDto(genre));
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
        if (!Objects.equals(name, genre.getName()) && genreRepository.findByName(name).isPresent()) {
            return error(String.format(GENRE_ALREADY_EXISTS, name));
        }
        genre.setName(name);
        genreRepository.save(genre);
        return done(genreMapper.toDto(genre));
    }

    @Transactional
    @Override
    public ServiceResponse<GenreDto> deleteGenre(long id) {
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
    public Genre findGenre(Long genreId) throws EntityNotFoundException {
        return ServiceUtils.findById(genreId, genreRepository::findById, GENRE_NOT_FOUND);
    }
}
