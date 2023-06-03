package ru.otus.homework.books.repository;

import jakarta.persistence.PersistenceException;
import lombok.val;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.homework.books.domain.Genre;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.otus.homework.books.domain.Book.FK_BOOK_GENRE;
import static ru.otus.homework.books.domain.Genre.UK_GENRE_NAME;

@DisplayName("Репозиторий жанров")
@DataJpaTest
@Import(GenreRepositoryJpa.class)
public
class GenreRepositoryJpaTest {

    public static final String AUTOBIOGRAPHY = "Автобиография";
    public static final String DETECTIVE = "Детектив";
    public static final String HISTORICAL_FICTION = "Исторический роман";
    public static final int NUMBER_OF_GENRES = 5;
    public static final long UNUSED_GENRE_ID = 2;
    public static final long DETECTIVE_GENRE_ID = 3;
    public static final long HISTORICAL_FICTION_ID = 5;

    @Autowired
    private TestEntityManager em;

    @Autowired
    private GenreRepository genreRepository;

    @Test
    void saveInsert() {
        val name = "New genre";
        val newGenre = new Genre(name);
        val genre = genreRepository.save(newGenre);
        assertThat(genreRepository.findByName(name)).isPresent()
                .map(Genre::getId).hasValue(genre.getId());
        assertThat(genreRepository.findById(genre.getId())).isPresent()
                .map(Genre::getName).hasValue(name);
    }

    @Test
    void saveInsertDuplicateName() {
        val newGenre = new Genre(DETECTIVE);
        val persistenceException = assertThrows(PersistenceException.class,
                () -> genreRepository.save(newGenre));
        assertThat(persistenceException)
                .hasCauseExactlyInstanceOf(ConstraintViolationException.class);
        var cause = (ConstraintViolationException) persistenceException.getCause();
        assertThat(cause.getConstraintName()).containsIgnoringCase(UK_GENRE_NAME);
    }

    @Test
    void saveUpdate() {
        val newName = "New genre";
        val genre4update = new Genre(DETECTIVE_GENRE_ID, newName);
        val genre = genreRepository.save(genre4update);
        assertThat(genreRepository.findByName(newName)).isPresent()
                .map(Genre::getId).hasValue(genre.getId());
    }

    @Test
    void saveUpdateDuplicateName() {
        val optGenre = genreRepository.findById(UNUSED_GENRE_ID);
        optGenre.ifPresent(g -> g.setName(HISTORICAL_FICTION));
        assertTrue(optGenre.isPresent());
        val origGenre = optGenre.get();
        origGenre.setName(HISTORICAL_FICTION);
        genreRepository.save(origGenre);

        val persistenceException = assertThrows(PersistenceException.class,
                () -> genreRepository.findByName(HISTORICAL_FICTION));

        assertThat(persistenceException)
                .hasCauseExactlyInstanceOf(ConstraintViolationException.class);
        var cause = (ConstraintViolationException) persistenceException.getCause();
        assertThat(cause.getConstraintName()).containsIgnoringCase(UK_GENRE_NAME);
    }

    @Test
    void findAll() {
        var genres = genreRepository.findAll();
        assertThat(genres).isNotNull().hasSize(NUMBER_OF_GENRES);
    }

    @Test
    void deleteById() {
        genreRepository.deleteById(UNUSED_GENRE_ID);
        assertEquals(NUMBER_OF_GENRES - 1, genreRepository.count());
    }

    @Test
    void delete() {
        genreRepository.delete(new Genre(UNUSED_GENRE_ID, AUTOBIOGRAPHY));
        assertEquals(NUMBER_OF_GENRES - 1, genreRepository.count());
    }

    @Test
    void deleteTestCascadeFailed() {
        val genre = new Genre(DETECTIVE_GENRE_ID, DETECTIVE);
        genreRepository.delete(genre);
        val persistenceException = assertThrows(PersistenceException.class, genreRepository::count);
        assertThat(persistenceException)
                .hasCauseExactlyInstanceOf(ConstraintViolationException.class);
        var cause = (ConstraintViolationException) persistenceException.getCause();
        assertThat(cause.getConstraintName()).containsIgnoringCase(FK_BOOK_GENRE);
    }
}