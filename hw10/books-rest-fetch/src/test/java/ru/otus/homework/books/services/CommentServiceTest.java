package ru.otus.homework.books.services;

import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.homework.books.domain.Author;
import ru.otus.homework.books.domain.Book;
import ru.otus.homework.books.domain.Comment;
import ru.otus.homework.books.domain.Genre;
import ru.otus.homework.books.mappers.AuthorMapper;
import ru.otus.homework.books.mappers.BookMapper;
import ru.otus.homework.books.mappers.CommentMapper;
import ru.otus.homework.books.mappers.CommentMapperImpl;
import ru.otus.homework.books.mappers.GenreMapper;
import ru.otus.homework.books.repository.AuthorRepository;
import ru.otus.homework.books.repository.BookRepository;
import ru.otus.homework.books.repository.CommentRepository;
import ru.otus.homework.books.repository.GenreRepository;
import ru.otus.homework.books.rest.dto.CommentDto;
import ru.otus.homework.books.services.misc.EntityNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static ru.otus.homework.books.services.ServiceResponse.Status.ERROR;
import static ru.otus.homework.books.services.ServiceResponse.Status.OK;
import static ru.otus.homework.books.services.misc.ServiceErrorMessages.getBookNotFoundMessage;
import static ru.otus.homework.books.services.misc.ServiceErrorMessages.getCommentNotFoundMessage;

@DisplayName("API для работы с комментариями")
@SpringBootTest(properties = "spring.profiles.active=test",
        classes = {CommentServiceImpl.class, CommentMapperImpl.class})
@MockBean(classes = {AuthorRepository.class, GenreRepository.class,
        BookRepository.class, BookMapper.class, GenreMapper.class, AuthorMapper.class})
class CommentServiceTest {

    @MockBean
    BookService bookService;
    @MockBean
    CommentRepository commentRepository;
    @Autowired
    CommentMapper commentMapper;
    @Autowired
    CommentService commentService;

    @DisplayName("Добавить комментарий к существующий книге")
    @Test
    void addComment() throws Exception {
        val textOfComment = "Text of new comment";
        // Entity
        val book = createBook();
        val comment = new Comment(1L, textOfComment, null);
        book.addComment(comment);
        // Mock
        given(bookService.findBook(book.getId())).willReturn(book);
        given(commentRepository.save(any(Comment.class))).willReturn(comment);
        // Check
        val response = commentService.addComment(book.getId(), new CommentDto(textOfComment));
        assertThat(response).isNotNull()
                .returns(OK, ServiceResponse::getStatus)
                .returns(null, ServiceResponse::getMessage)
                .extracting(ServiceResponse::getData).isNotNull()
                .returns(1L, CommentDto::getId)
                .returns(textOfComment, CommentDto::getText);
    }

    @DisplayName("Добавить комментарий к несуществующий книге")
    @Test
    void addCommentNotFoundException() throws Exception {
        val textOfComment = "Does not matter";
        // Entity
        val book = createBook();
        val comment = new Comment(1L, textOfComment, book);
        book.addComment(comment);
        // Mock
        given(commentRepository.findById(comment.getId())).willReturn(Optional.of(comment));
        given(commentRepository.save(any(Comment.class))).willReturn(comment);
        given(bookService.findBook(book.getId())).willThrow(new EntityNotFoundException(getBookNotFoundMessage(book.getId())));
        // Check
        val response = commentService.addComment(book.getId(), new CommentDto(textOfComment));
        assertThat(response).isNotNull()
                .returns(ERROR, ServiceResponse::getStatus)
                .returns(getBookNotFoundMessage(book.getId()), ServiceResponse::getMessage)
                .extracting(ServiceResponse::getData).isNull();
    }

    @DisplayName("Изменить существующий комментарий")
    @Test
    void modifyComment() {
        long id = 1L;
        val textOfComment = "New text of comment";
        // Entity
        val book = createBook();
        val comment = new Comment(1L, "Current text of comment", book);
        book.addComment(comment);
        // Mock
        given(commentRepository.findById(comment.getId())).willReturn(Optional.of(comment));
        given(commentRepository.save(any(Comment.class))).willReturn(comment);
        // Check
        val response = commentService.modifyComment(new CommentDto(id, textOfComment));
        assertThat(response).isNotNull()
                .returns(OK, ServiceResponse::getStatus)
                .returns(null, ServiceResponse::getMessage)
                .extracting(ServiceResponse::getData).isNotNull()
                .returns(id, CommentDto::getId)
                .returns(textOfComment, CommentDto::getText);
    }


    @DisplayName("Изменить несуществующий комментарий")
    @Test
    void modifyCommentEntityNotFoundException() {
        long id = 1L;
        // Mock
        given(commentRepository.findById(id)).willReturn(Optional.empty());
        // Check
        val response = commentService.modifyComment(new CommentDto(id, "New text of comment"));
        assertThat(response).isNotNull()
                .returns(ERROR, ServiceResponse::getStatus)
                .returns(getCommentNotFoundMessage(id), ServiceResponse::getMessage)
                .extracting(ServiceResponse::getData).isNull();
    }

    @DisplayName("Удалить существующий комментарий")
    @Test
    void deleteComment() {
        long id = 1L;
        val textOfComment = "Comment text";
        // Entity
        val book = createBook();
        val comment = new Comment(1L, textOfComment, book);
        book.addComment(comment);
        // Mock
        given(commentRepository.findById(id)).willReturn(Optional.of(comment));
        // Check
        val response = commentService.deleteComment(id);
        assertThat(response).isNotNull()
                .returns(OK, ServiceResponse::getStatus)
                .returns(null, ServiceResponse::getMessage)
                .extracting(ServiceResponse::getData).isNotNull()
                .returns(id, CommentDto::getId)
                .returns(textOfComment, CommentDto::getText);
        assertThat(book.getComments()).isEmpty();
    }

    private static Book createBook() {
        val author = new Author(1L, "author1");
        val genre = new Genre(1L, "genre1");
        return new Book(1L, "book1", author, genre, null);
    }

}
