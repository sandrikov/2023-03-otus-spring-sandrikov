package ru.otus.homework.books.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import ru.otus.homework.books.model.Author;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Репозиторий на основе Jdbc для работы с авторами")
@JdbcTest
@Import({AuthorRepositoryJdbc.class})
public class AuthorRepositoryJdbcTest {

    public static final String AGATHA_CHRISTIE = "Агата Кристи";
    public static final String ALEXANDR_DUMAS = "Александр Дюма";
    public static final String MARK_TWAIN = "Марк Твен";
    public static final String LEO_TOLSTOY = "Лев Толстой";
    public static final int NUMBER_OF_AUTHORS = 4;
    public static final int NUMBER_OF_UNUSED_AUTHORS = 1;
    public static final long UNUSED_AUTHOR_ID = 3;
    public static final long MARK_TWAIN_ID = 1;

    @Autowired
    private AuthorRepository authorDao;

    @Test
    void count() {
        var count = authorDao.count();
        assertEquals(count, NUMBER_OF_AUTHORS);
    }

    @Test
    void saveInsert() {
        var name = "New author";
        var newAuthor = new Author(name);
        authorDao.save(newAuthor);

        var author = authorDao.findByName(name);
        assertTrue(author.isPresent());
        assertEquals(NUMBER_OF_AUTHORS + 1L, author.get().getId());
        assertEquals(name, author.get().getName());
    }

    @Test
    void saveInsertDuplicate() {
        var author = new Author(MARK_TWAIN);
        assertThrows(DuplicateKeyException.class, () -> authorDao.save(author));
    }

    @Test
    void saveUpdate() {
        var authorToModify = authorDao.findById(MARK_TWAIN_ID);
        assertEquals(MARK_TWAIN, authorToModify.getName());

        var newName = "New author";
        authorToModify.setName(newName);
        authorDao.save(authorToModify);

        var author = authorDao.findById(MARK_TWAIN_ID);
        assertEquals(newName, author.getName());
    }
    @Test
    void findAllUsed() {
        var authors = authorDao.findAll();
        assertThat(authors).isNotNull().hasSize(NUMBER_OF_AUTHORS);
    }

    @Test
    void findAll() {
        var authors = authorDao.findAllUsed();
        assertThat(authors).isNotNull().hasSize(NUMBER_OF_AUTHORS - NUMBER_OF_UNUSED_AUTHORS);
    }

    @Test
    void findById() {
        var authorToModify = authorDao.findById(MARK_TWAIN_ID);
        assertEquals(MARK_TWAIN, authorToModify.getName());
    }

    @Test
    void findByName() {
        var author = authorDao.findByName(MARK_TWAIN);
        assertTrue(author.isPresent());
        assertEquals(MARK_TWAIN_ID, author.get().getId());
    }

    @Test
    void deleteById() {
        authorDao.deleteById(UNUSED_AUTHOR_ID);
        assertEquals(NUMBER_OF_AUTHORS - 1, authorDao.count());
        assertThrows(EmptyResultDataAccessException.class, () -> authorDao.findById(UNUSED_AUTHOR_ID));
    }

    @Test
    void deleteByIdKO() {
        assertThrows(EmptyResultDataAccessException.class, () -> authorDao.deleteById(99));
    }


    @Test
    void delete() {
        var author = authorDao.findById(UNUSED_AUTHOR_ID);
        authorDao.delete(author);
        assertEquals(NUMBER_OF_AUTHORS - 1, authorDao.count());
        assertThrows(EmptyResultDataAccessException.class, () -> authorDao.findById(UNUSED_AUTHOR_ID));
    }

    @Test
    void deleteByIdDataIntegrityViolationException() {
        assertThrows(DataIntegrityViolationException.class, () -> authorDao.deleteById(MARK_TWAIN_ID));
    }
}