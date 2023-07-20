package ru.otus.homework.books.repository;


import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import ru.otus.homework.books.domain.Comment;
import ru.otus.homework.books.mappers.AuthorMapper;
import ru.otus.homework.books.mappers.BookMapperImpl;
import ru.otus.homework.books.mappers.CommentMapperImpl;
import ru.otus.homework.books.mappers.GenreMapper;
import ru.otus.homework.books.services.AuthorService;
import ru.otus.homework.books.services.BookService;
import ru.otus.homework.books.services.BookServiceImpl;
import ru.otus.homework.books.services.CommentService;
import ru.otus.homework.books.services.GenreService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.otus.homework.books.repository.AuthorRepositoryTest.MARK_TWAIN;
import static ru.otus.homework.books.repository.BookRepositoryTest.JEANNE_D_ARC;
import static ru.otus.homework.books.repository.BookRepositoryTest.TWAIN_D_ARK_BOOK_ID;

@DisplayName("Репозиторий для работы с комментариями")
@DataJpaTest
@MockBean({GenreMapper.class, AuthorMapper.class, AuthorService.class, GenreService.class, CommentService.class})
@Import({CommentMapperImpl.class, BookMapperImpl.class, BookServiceImpl.class, BookRepositoryCustomImpl.class})
public class CommentRepositoryTest {

    public static final int NUMBER_OF_TWAIN_D_ARK_COMMENTS = 2;
    public static final long TWAIN_D_ARK_1ST_COMMENT_ID = 6;
    public static final long TWAIN_D_ARK_2ND_COMMENT_ID = 7;

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private BookService bookService;
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
    void saveComment() throws Exception {
        val text = "Comment #3 " + JEANNE_D_ARC + " of " + MARK_TWAIN;

        val book = bookService.findBook(TWAIN_D_ARK_BOOK_ID);
        val commentToAdd = new Comment(text, book);
        commentRepository.save(commentToAdd);
        em.flush();
        em.clear();

        val bookAfterSave = bookService.findBook(TWAIN_D_ARK_BOOK_ID);
        val comments = bookAfterSave.getComments();
        assertEquals(NUMBER_OF_TWAIN_D_ARK_COMMENTS + 1, comments.size(),
                "Number of comments");
        assertEquals(text, comments.get(NUMBER_OF_TWAIN_D_ARK_COMMENTS).getText(),
                "Last comment is our");
    }

    @Test
    void deleteComment() throws Exception {
        val comment1st = commentRepository.findById(TWAIN_D_ARK_1ST_COMMENT_ID);
        assertTrue(comment1st.isPresent());
        val commentToDelete = comment1st.get();
        commentRepository.delete(commentToDelete);
        em.flush();
        em.clear();

        val bookAfterSave = bookService.findBook(TWAIN_D_ARK_BOOK_ID);
        val comments = bookAfterSave.getComments();
        assertEquals(NUMBER_OF_TWAIN_D_ARK_COMMENTS - 1, comments.size(),
                "Number of comments");
        assertEquals(TWAIN_D_ARK_2ND_COMMENT_ID,
                comments.get(NUMBER_OF_TWAIN_D_ARK_COMMENTS - 2).getId(),
                "Last comment is our");
    }

}
