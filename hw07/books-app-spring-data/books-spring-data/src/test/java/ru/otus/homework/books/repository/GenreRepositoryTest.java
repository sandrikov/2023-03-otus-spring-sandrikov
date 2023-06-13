package ru.otus.homework.books.repository;

import lombok.val;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import ru.otus.homework.books.domain.Genre;
import ru.otus.homework.books.mappers.BookMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.otus.homework.books.domain.SchemaSqlConstants.FK_BOOK_GENRE;
import static ru.otus.homework.books.domain.SchemaSqlConstants.UK_GENRE_NAME;

@DisplayName("Репозиторий жанров")
@DataJpaTest
@MockBean(BookMapper.class)
public class GenreRepositoryTest {

    public static final String AUTOBIOGRAPHY = "Автобиография";
    public static final String DETECTIVE = "Детектив";
    public static final String HISTORICAL_FICTION = "Исторический роман";
    public static final long UNUSED_GENRE_ID = 2;
    public static final long DETECTIVE_GENRE_ID = 3;
    public static final long HISTORICAL_FICTION_ID = 5;

    @Autowired
    private GenreRepository genreRepository;
    @Test
    void saveInsertDuplicateName() {
        val newGenre = new Genre(DETECTIVE);
        val persistenceException = assertThrows(DataIntegrityViolationException.class,
                () -> genreRepository.save(newGenre));
        assertThat(persistenceException)
                .hasCauseExactlyInstanceOf(ConstraintViolationException.class);
        var cause = (ConstraintViolationException) persistenceException.getCause();
        assertThat(cause.getConstraintName()).containsIgnoringCase(UK_GENRE_NAME);
    }

    @Test
    void saveUpdateDuplicateName() {
        val optGenre = genreRepository.findById(UNUSED_GENRE_ID);
        optGenre.ifPresent(g -> g.setName(HISTORICAL_FICTION));
        assertTrue(optGenre.isPresent());
        val origGenre = optGenre.get();
        origGenre.setName(HISTORICAL_FICTION);
        genreRepository.save(origGenre);

        val persistenceException = assertThrows(DataIntegrityViolationException.class,
                () -> genreRepository.findByName(HISTORICAL_FICTION));

        assertThat(persistenceException)
                .hasCauseExactlyInstanceOf(ConstraintViolationException.class);
        var cause = (ConstraintViolationException) persistenceException.getCause();
        assertThat(cause.getConstraintName()).containsIgnoringCase(UK_GENRE_NAME);
    }

    @Test
    void deleteTestCascadeFailed() {
        val genre = new Genre(DETECTIVE_GENRE_ID, DETECTIVE);
        genreRepository.delete(genre);
        val persistenceException = assertThrows(DataIntegrityViolationException.class, genreRepository::count);
        assertThat(persistenceException)
                .hasCauseExactlyInstanceOf(ConstraintViolationException.class);
        var cause = (ConstraintViolationException) persistenceException.getCause();
        assertThat(cause.getConstraintName()).containsIgnoringCase(FK_BOOK_GENRE);
    }
}