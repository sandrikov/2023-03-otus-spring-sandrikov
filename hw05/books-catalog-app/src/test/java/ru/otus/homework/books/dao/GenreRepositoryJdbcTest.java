package ru.otus.homework.books.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import ru.otus.homework.books.model.Genre;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Репозиторий на основе Jdbc для работы с жанрами")
@JdbcTest
@Import({GenreRepositoryJdbc.class})
public class GenreRepositoryJdbcTest {

    public static final String AUTOBIOGRAPHY = "Автобиография";
    public static final String DETECTIVE = "Детектив";
    public static final String HISTORICAL_FICTION = "Исторический роман";
    public static final String HISTORICAL_CHRONICLES = "Исторические хроники";
    public static final int NUMBER_OF_GENRES = 5;
    public static final int NUMBER_OF_UNUSED_GENRES = 1;
    public static final long UNUSED_GENRE_ID = 2;
    public static final long DETECTIVE_GENRE_ID = 3;

    @Autowired
    private GenreRepository genreDao;
    @Test
    void count() {
        var count = genreDao.count();
        assertEquals(count, NUMBER_OF_GENRES);
    }

    @Test
    void saveInsert() {
        var name = "New genre";
        var newGenre = new Genre(name);
        genreDao.save(newGenre);

        var genre = genreDao.findByName(name);
        assertTrue(genre.isPresent());
        assertEquals(NUMBER_OF_GENRES + 1L, genre.get().getId());
        assertEquals(name, genre.get().getName());
    }

    @Test
    void saveUpdate() {
        var genreToModify = genreDao.findById(DETECTIVE_GENRE_ID);
        assertEquals(DETECTIVE, genreToModify.getName());

        var newName = "New genre";
        genreToModify.setName(newName);
        genreDao.save(genreToModify);

        var genre = genreDao.findById(DETECTIVE_GENRE_ID);
        assertEquals(newName, genre.getName());
    }

    @Test
    void saveUpdateDuplicateKeyException() {
        var genre = genreDao.findById(DETECTIVE_GENRE_ID);
        assertEquals(DETECTIVE, genre.getName());
        genre.setName(HISTORICAL_FICTION);
        assertThrows(DuplicateKeyException.class, () -> genreDao.save(genre));
    }

    @Test
    void findById() {
        var genre = genreDao.findById(DETECTIVE_GENRE_ID);
        assertEquals(DETECTIVE, genre.getName());
    }

    @Test
    void findByName() {
        var genre = genreDao.findByName(DETECTIVE);
        assertTrue(genre.isPresent());
        assertEquals(DETECTIVE_GENRE_ID, genre.get().getId());
    }

    @Test
    void findAllUsed() {
        var genres = genreDao.findAllUsed();
        assertThat(genres).isNotNull().hasSize(NUMBER_OF_GENRES - NUMBER_OF_UNUSED_GENRES);
    }

    @Test
    void findAll() {
        var genres = genreDao.findAll();
        assertThat(genres).isNotNull().hasSize(NUMBER_OF_GENRES);
    }

    @Test
    void deleteById() {
        genreDao.deleteById(UNUSED_GENRE_ID);
        assertEquals(NUMBER_OF_GENRES - 1, genreDao.count());
        assertThrows(EmptyResultDataAccessException.class, () -> genreDao.findById(UNUSED_GENRE_ID));
    }

    @Test
    void deleteByIdDataIntegrityViolationException() {
        assertThrows(DataIntegrityViolationException.class, () -> genreDao.deleteById(DETECTIVE_GENRE_ID));
    }

    @Test
    void delete() {
        var genre = genreDao.findById(UNUSED_GENRE_ID);
        genreDao.delete(genre);
        assertEquals(NUMBER_OF_GENRES - 1, genreDao.count());
        assertThrows(EmptyResultDataAccessException.class, () -> genreDao.findById(UNUSED_GENRE_ID));
    }
}