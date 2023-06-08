package ru.otus.homework.books.repository;

import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.homework.books.domain.Comment;
import ru.otus.homework.books.mappers.CommentMapperImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.otus.homework.books.repository.AuthorRepositoryJpaTest.MARK_TWAIN;
import static ru.otus.homework.books.repository.BookRepositoryJpaTest.JEANNE_D_ARC;
import static ru.otus.homework.books.repository.BookRepositoryJpaTest.NUMBER_OF_TWAIN_D_ARK_COMMENTS;
import static ru.otus.homework.books.repository.BookRepositoryJpaTest.TWAIN_D_ARK_1ST_COMMENT_ID;
import static ru.otus.homework.books.repository.BookRepositoryJpaTest.TWAIN_D_ARK_2ND_COMMENT_ID;
import static ru.otus.homework.books.repository.BookRepositoryJpaTest.TWAIN_D_ARK_BOOK_ID;


@DisplayName("Репозиторий для работы с комментариями")
@DataJpaTest
@Import({CommentRepositoryJpa.class, CommentMapperImpl.class, BookRepositoryJpa.class})
class CommentRepositoryJpaTest {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private TestEntityManager em;

    @Test
    void findCommentById() {
        val comment2nd = commentRepository.findById(TWAIN_D_ARK_2ND_COMMENT_ID);
        assertTrue(comment2nd.isPresent());
        val comment = comment2nd.get();
        assertEquals(TWAIN_D_ARK_BOOK_ID, comment.getBook().getId(), "Book Id");
        assertEquals(NUMBER_OF_TWAIN_D_ARK_COMMENTS, comment.getBook().getComments().size(),
                "Number of comments");
    }

    @Test
    void saveComment() {
        val text = "Comment #3 " + JEANNE_D_ARC + " of " + MARK_TWAIN;

        val book = bookRepository.findById(TWAIN_D_ARK_BOOK_ID);
        assertTrue(book.isPresent());
        val commentToAdd = new Comment(text, book.get());
        commentRepository.save(commentToAdd);
        em.flush();
        em.clear();

        val bookAfterSave = bookRepository.findById(TWAIN_D_ARK_BOOK_ID);
        assertTrue(bookAfterSave.isPresent());
        val comments = bookAfterSave.get().getComments();
        assertEquals(NUMBER_OF_TWAIN_D_ARK_COMMENTS + 1, comments.size(),
                "Number of comments");
        assertEquals(text, comments.get(NUMBER_OF_TWAIN_D_ARK_COMMENTS).getText(),
                "Last comment is our");
    }

    @Test
    void deleteComment() {
        val comment1st = commentRepository.findById(TWAIN_D_ARK_1ST_COMMENT_ID);
        assertTrue(comment1st.isPresent());
        val commentToDelete = comment1st.get();
        commentRepository.delete(commentToDelete);
        em.flush();
        em.clear();

        val bookAfterSave = bookRepository.findById(TWAIN_D_ARK_BOOK_ID);
        assertTrue(bookAfterSave.isPresent());
        val comments = bookAfterSave.get().getComments();
        assertEquals(NUMBER_OF_TWAIN_D_ARK_COMMENTS - 1, comments.size(),
                "Number of comments");
        assertEquals(TWAIN_D_ARK_2ND_COMMENT_ID,
                comments.get(NUMBER_OF_TWAIN_D_ARK_COMMENTS - 2).getId(),
                "Last comment is our");
    }

}