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
import ru.otus.homework.books.dto.CommentDto;
import ru.otus.homework.books.mappers.AuthorMapper;
import ru.otus.homework.books.mappers.BookMapper;
import ru.otus.homework.books.mappers.BookMapperImpl;
import ru.otus.homework.books.mappers.CommentMapper;
import ru.otus.homework.books.mappers.CommentMapperImpl;
import ru.otus.homework.books.mappers.GenreMapper;
import ru.otus.homework.books.repository.AuthorRepository;
import ru.otus.homework.books.repository.BookRepository;
import ru.otus.homework.books.repository.GenreRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static ru.otus.homework.books.services.BookServiceImpl.BOOK_NOT_FOUND;
import static ru.otus.homework.books.services.BookServiceImpl.COMMENT_NOT_FOUND;
import static ru.otus.homework.books.services.ServiceResponse.Status.ERROR;
import static ru.otus.homework.books.services.ServiceResponse.Status.OK;

@DisplayName("API для работы с комментариями. Юнит тест")
@SpringBootTest(properties = "spring.profiles.active=test",
        classes = {BookServiceImpl.class, CommentMapperImpl.class, BookMapperImpl.class})
@MockBean(classes = {AuthorRepository.class, GenreRepository.class,
        BookMapper.class, GenreMapper.class, AuthorMapper.class})
class BookServiceTest {

    @MockBean
    BookRepository repository;
    @Autowired
    CommentMapper commentMapper;
    @Autowired
    BookService bookService;

    @DisplayName("Добавить комментарий к существующий книге")
    @Test
    void addComment() {
        val textOfComment = "Text of new comment";
        // Entity
        val book = createBook();
        val comment = new Comment(1L, textOfComment, null);
        book.addComment(comment);
        // Mock
        given(repository.findById(book.getId())).willReturn(Optional.of(book));
        given(repository.saveComment(any(Book.class), any(Comment.class))).willReturn(comment);
        // Check
        val response = bookService.addComment(book.getId(), new CommentDto(textOfComment));
        assertThat(response).isNotNull()
                .returns(OK, ServiceResponse::getStatus)
                .returns(null, ServiceResponse::getMessage)
                .extracting(ServiceResponse::getData).isNotNull()
                .returns(1L, CommentDto::getId)
                .returns(textOfComment, CommentDto::getText);
    }

    @DisplayName("Добавить комментарий к несуществующий книге")
    @Test
    void addCommentNotFoundException() {
        val textOfComment = "Does not matter";
        // Entity
        val book = createBook();
        val comment = new Comment(1L, textOfComment, null);
        book.addComment(comment);
        // Mock
        given(repository.findById(book.getId())).willReturn(Optional.empty());
        // Check
        val response = bookService.addComment(book.getId(), new CommentDto(textOfComment));
        assertThat(response).isNotNull()
                .returns(ERROR, ServiceResponse::getStatus)
                .returns(String.format(BOOK_NOT_FOUND, "id=" + book.getId()), ServiceResponse::getMessage)
                .extracting(ServiceResponse::getData).isNull();
    }

    @DisplayName("Изменить существующий комментарий")
    @Test
    void modifyComment() {
        long id = 1L;
        val textOfComment = "New text of comment";
        // Entity
        val comment = new Comment(id, "Current text of comment", createBook());
        // Mock
        given(repository.findCommentById(id)).willReturn(Optional.of(comment));
        given(repository.saveComment(any(Book.class), any(Comment.class))).willReturn(comment);
        // Check
        val response = bookService.modifyComment(new CommentDto(id, textOfComment));
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
        given(repository.findCommentById(id)).willReturn(Optional.empty());
        // Check
        val response = bookService.modifyComment(new CommentDto(id, "New text of comment"));
        assertThat(response).isNotNull()
                .returns(ERROR, ServiceResponse::getStatus)
                .returns(String.format(COMMENT_NOT_FOUND, "id=" + id), ServiceResponse::getMessage)
                .extracting(ServiceResponse::getData).isNull();
    }

    @DisplayName("Удалить существующий комментарий")
    @Test
    void deleteComment() {
        long id = 1L;
        val textOfComment = "Comment text";
        // Entity
        val comment = new Comment(id, textOfComment, createBook());
        // Mock
        given(repository.findCommentById(id)).willReturn(Optional.of(comment));
        // Check
        val response = bookService.deleteComment(id);
        assertThat(response).isNotNull()
                .returns(OK, ServiceResponse::getStatus)
                .returns(null, ServiceResponse::getMessage)
                .extracting(ServiceResponse::getData).isNotNull()
                .returns(id, CommentDto::getId)
                .returns(textOfComment, CommentDto::getText);
    }

    private static Book createBook() {
        val author = new Author(1L, "author1");
        val genre = new Genre(1L, "genre1");
        return new Book(1L, "book1", author, genre, null);
    }

}
