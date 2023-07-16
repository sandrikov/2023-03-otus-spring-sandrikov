package ru.otus.homework.books.repository;

import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import reactor.test.StepVerifier;
import ru.otus.homework.books.domain.Author;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static ru.otus.homework.books.domain.SchemaSqlConstants.FK_BOOK_AUTHOR;
import static ru.otus.homework.books.misc.TestConstants.JAMES_HADLEY_CHASE;
import static ru.otus.homework.books.misc.TestConstants.JAMES_HADLEY_CHASE_ID;
import static ru.otus.homework.books.misc.TestConstants.LEO_TOLSTOY;
import static ru.otus.homework.books.misc.TestConstants.LEO_TOLSTOY_ID;
import static ru.otus.homework.books.misc.TestConstants.MARK_TWAIN;
import static ru.otus.homework.books.misc.TestConstants.MARK_TWAIN_ID;


@DisplayName("Репозиторий авторов книг")
@SpringBootTest
public class AuthorRepositoryTest {

    @Autowired
    private AuthorRepository repository;

    @Test
    void shouldSetIdOnSave() {
        val entity = new Author("test");
        StepVerifier.create(repository.save(entity))
                .assertNext(t -> {
                    assertNotNull(t.getId());
                    assertEquals(entity.getName(), t.getName());
                })
                .verifyComplete();
    }    

    @Test
    void saveFindAndUpdateName() {
        StepVerifier.create(repository.findById(JAMES_HADLEY_CHASE_ID))
                .assertNext(t -> assertEquals(JAMES_HADLEY_CHASE, t.getName()))
                .verifyComplete();
        val newName = "New name";
        val entity = new Author(JAMES_HADLEY_CHASE_ID, newName);
        StepVerifier.create(repository.save(entity))
                .assertNext(t -> assertEquals(newName, t.getName()))
                .verifyComplete();
        StepVerifier.create(repository.findById(JAMES_HADLEY_CHASE_ID))
                .assertNext(t -> assertEquals(newName, t.getName()))
                .verifyComplete();
    }

    @Test
    void deleteAndCheck() {
        val entity = new Author(LEO_TOLSTOY_ID, LEO_TOLSTOY); // has no books
        StepVerifier.create(repository.delete(entity)).verifyComplete();
        StepVerifier.create(repository.findById(entity.getId()))
                .expectNextCount(0).verifyComplete();
    }

    @Test
    void deleteCascadeFailed() {
        val entity = new Author(MARK_TWAIN_ID, MARK_TWAIN);
        StepVerifier.create(repository.delete(entity))
                .verifyErrorMatches(throwable -> throwable instanceof DataIntegrityViolationException &&
                        throwable.getMessage().contains(FK_BOOK_AUTHOR));
    }

}