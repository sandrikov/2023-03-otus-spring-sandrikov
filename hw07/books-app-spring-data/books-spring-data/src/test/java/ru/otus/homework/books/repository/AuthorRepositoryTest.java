package ru.otus.homework.books.repository;

import lombok.val;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import ru.otus.homework.books.domain.Author;
import ru.otus.homework.books.mappers.BookProjectionMapper;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.otus.homework.books.domain.Author.UK_AUTHOR_NAME;
import static ru.otus.homework.books.domain.Book.FK_BOOK_AUTHOR;


@DisplayName("Репозиторий авторов книг")
@DataJpaTest
@MockBean(classes = {BookProjectionMapper.class})
public
class AuthorRepositoryTest {

    public static final String AGATHA_CHRISTIE = "Агата Кристи";
    public static final String ALEXANDR_DUMAS = "Александр Дюма";
    public static final String MARK_TWAIN = "Марк Твен";
    public static final String LEO_TOLSTOY = "Лев Толстой";
    public static final int NUMBER_OF_AUTHORS = 4;
    public static final long UNUSED_AUTHOR_ID = 3;
    public static final long MARK_TWAIN_ID = 1;

    @Autowired
    private TestEntityManager em;

    @Autowired
    private AuthorRepository authorRepository;

    @Test
    void saveInsert() {
        val name = "New author";
        val name2 = "New author 2";
        assertThat(authorRepository.findByName(name)).isEmpty();
        assertThat(authorRepository.findAll())
                .allSatisfy(a -> assertThat(a.getName())
                        .isNotEqualTo(name)
                        .isNotEqualTo(name2));

        val author = authorRepository.save(new Author(name));
        val author2 = authorRepository.save(new Author(name2));

        assertThat(authorRepository.findByName(name)).isPresent()
                .map(Author::getId).hasValue(author.getId());
        assertThat(authorRepository.findByName(name2)).isPresent()
                .map(Author::getId).hasValue(author2.getId());

        em.flush();
        em.refresh(author);
        em.refresh(author2);
        assertThat(authorRepository.findByName(name)).isPresent()
                .map(Author::getId).hasValue(author.getId());
        assertThat(authorRepository.findByName(name2)).isPresent()
                .map(Author::getId).hasValue(author2.getId());
    }

    @Test
    void saveInsertDuplicateName() {
        var author = new Author(MARK_TWAIN);
        val persistenceException = assertThrows(DataIntegrityViolationException.class, () -> authorRepository.save(author));
        assertThat(persistenceException)
                .hasCauseExactlyInstanceOf(ConstraintViolationException.class);
        var cause = (ConstraintViolationException) persistenceException.getCause();
        assertThat(cause.getConstraintName()).containsIgnoringCase(UK_AUTHOR_NAME);
    }

    @Test
    void saveFindAndUpdateName() {
        val authorToModify = authorRepository.findById(MARK_TWAIN_ID);
        assertThat(authorToModify).isPresent()
                .map(Author::getName).hasValue(MARK_TWAIN);

        val newName = "New author's name";
        authorToModify.get().setName(newName);
        authorRepository.save(authorToModify.get());

        assertThat(authorRepository.findById(MARK_TWAIN_ID)).isPresent()
                .map(Author::getName).hasValue(newName);

    }

    @Test
    void saveUpdateNameWoFind() {
        val newName = "New author's name";
        val author4update = new Author(MARK_TWAIN_ID, newName);
        authorRepository.save(author4update);

        val authorFromContext = authorRepository.findByName(newName);
        assertThat(authorFromContext).isPresent().map(Author::getId).hasValue(MARK_TWAIN_ID);
        em.flush();

        val authorFromDb = em.refresh(authorFromContext.get());
        assertEquals(authorFromDb.getId(), MARK_TWAIN_ID);
        assertEquals(authorFromDb.getName(), newName);

        assertThat(authorRepository.findById(MARK_TWAIN_ID))
                .isPresent().map(Author::getName).hasValue(newName);
    }

    @Test
    void findAll() {
        assertThat(authorRepository.findAll()).isNotNull().hasSize(NUMBER_OF_AUTHORS);
    }

    @Test
    void delete() {
        var author = new Author(UNUSED_AUTHOR_ID, LEO_TOLSTOY);
        authorRepository.delete(author);
        assertEquals(NUMBER_OF_AUTHORS - 1, authorRepository.count());
        Optional<Author> authorById = authorRepository.findById(UNUSED_AUTHOR_ID);
        assertThat(authorById).isNotPresent();
    }

    @Test
    void deleteTestCascadeFailed() {
        val author = new Author(MARK_TWAIN_ID, MARK_TWAIN);
        authorRepository.delete(author);
        val persistenceException = assertThrows(DataIntegrityViolationException.class, authorRepository::count);
        assertThat(persistenceException)
                .hasCauseExactlyInstanceOf(ConstraintViolationException.class);
        var cause = (ConstraintViolationException) persistenceException.getCause();
        assertThat(cause.getConstraintName()).containsIgnoringCase(FK_BOOK_AUTHOR);
    }

}