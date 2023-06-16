package ru.otus.homework.books.controller;

import jakarta.servlet.ServletException;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.homework.books.dto.AuthorDto;
import ru.otus.homework.books.dto.BookDto;
import ru.otus.homework.books.dto.BookProjection;
import ru.otus.homework.books.dto.GenreDto;
import ru.otus.homework.books.services.AuthorService;
import ru.otus.homework.books.services.BookService;
import ru.otus.homework.books.services.GenreService;

import java.util.List;

import static java.util.Optional.ofNullable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static ru.otus.homework.books.services.BookServiceImpl.BOOK_NOT_FOUND;
import static ru.otus.homework.books.services.ServiceResponse.done;
import static ru.otus.homework.books.services.ServiceResponse.error;

@WebMvcTest(BookController.class)
@MockBean(classes = {AuthorService.class, GenreService.class})
class BookControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookService bookService;
    @MockBean
    private AuthorService authorService;
    @MockBean
    private GenreService genreService;

    @DisplayName("Вывести список из двух книг")
    @Test
    void listPage() throws Exception {
        val books = List.of(createBookProjection(1), createBookProjection(2));
        given(bookService.listBooks(null, null, null)).willReturn(done(books));

        mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("bookList"))
                .andExpect(model().attributeExists("books"))
                .andExpect(model().attribute("books", books));
    }

    @DisplayName("Показать страницу редактирования книги")
    @Test
    void editPage() throws Exception {
        val book = createBookProjection(1L);
        val authors = List.of(book.author());
        val genres = List.of(book.genre());
        given(authorService.listAuthors()).willReturn(done(authors));
        given(genreService.listGenres()).willReturn(done(genres));
        given(bookService.getBookProjection(book.id())).willReturn(done(book));
        mvc.perform(get("/book/1/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("bookEdit"))
                .andExpect(model().attributeExists("book"))
                .andExpect(model().attribute("book", book))
                .andExpect(model().attributeExists("authors"))
                .andExpect(model().attribute("authors", authors))
                .andExpect(model().attributeExists("genres"))
                .andExpect(model().attribute("genres", genres));
    }

    @DisplayName("Попытка открыть страницу редактирования несуществующей книги")
    @Test
    void editPageEntityNotFoundException() {
        val message = String.format(BOOK_NOT_FOUND, "id=1");
        given(bookService.getBookProjection(1L)).willReturn(error(message));
        val servletException = assertThrows(ServletException.class,
                () -> mvc.perform(get("/book/1/edit")));
        assertThat(servletException)
                .hasCauseExactlyInstanceOf(BookAppException.class);
        var cause = (BookAppException) servletException.getCause();
        assertThat(cause.getMessage()).isEqualTo(message);
    }

    @DisplayName("Успешно отредактировать книгу")
    @Test
    void saveBook() throws Exception {
        val book = createBookDto(1L);
        given(bookService.modifyBook(any())).willReturn(done(book));
        mvc.perform(post("/book/1/edit")
                        .contentType(APPLICATION_FORM_URLENCODED)
                        .param("title", book.getTitle())
                        .param("author.id", Long.toString(book.getAuthor().getId()))
                        .param("genre.id", Long.toString(book.getGenre().getId())))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/"));
    }

    @DisplayName("Показать страницу добавления книги")
    @Test
    void addBookPage() throws Exception {
        val book = createBookProjection(0L);
        val authors = List.of(book.author());
        val genres = List.of(book.genre());
        given(authorService.listAuthors()).willReturn(done(authors));
        given(genreService.listGenres()).willReturn(done(genres));
        mvc.perform(get("/book/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("bookAdd"))
                .andExpect(model().attributeExists("authors"))
                .andExpect(model().attribute("authors", authors))
                .andExpect(model().attributeExists("genres"))
                .andExpect(model().attribute("genres", genres));
    }

    @DisplayName("Успешно добавить книгу")
    @Test
    void addBook() throws Exception {
        val book = createBookDto(1L);
        given(bookService.createBook(any(), any(), any()))
                .willReturn(done(toBookProjection(book)));
        mvc.perform(post("/book/add")
                        .contentType(APPLICATION_FORM_URLENCODED)
                        .param("title", book.getTitle())
                        .param("author.id", Long.toString(book.getAuthor().getId()))
                        .param("genre.id", Long.toString(book.getGenre().getId())))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/"));
    }

    @DisplayName("Попытка добавить книгу с пустым названием")
    @Test
    void addBookWrongTitle() throws Exception {
        given(authorService.listAuthors()).willReturn(done(List.of(new AuthorDto(1L, "Author1"))));
        given(genreService.listGenres()).willReturn(done(List.of(new GenreDto(1L, "Genre1"))));
        mvc.perform(post("/book/add")
                        .contentType(APPLICATION_FORM_URLENCODED)
                        .param("title", " ")) // should not be blank
                .andExpect(status().isOk())
                .andExpect(view().name("bookAdd")); // go back
    }

    @DisplayName("Показать страницу удаления книги")
    @Test
    void deletePage() throws Exception {
        val book = createBookDto(2L);
        given(bookService.getBook(book.getId())).willReturn(done(book));
        mvc.perform(get("/book/{id}/delete", Long.toString(book.getId())))
                .andExpect(status().isOk())
                .andExpect(view().name("bookDelete"))
                .andExpect(model().attributeExists("book"))
                .andExpect(model().attribute("book", book));
    }

    @DisplayName("Успешно удалить книгу")
    @Test
    void deleteBook() throws Exception {
        val book = createBookProjection(2L);
        given(bookService.deleteBook(book.id())).willReturn(done(book));
        mvc.perform(delete("/book/{id}/delete", Long.toString(book.id())))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/"));
    }

    private static BookProjection createBookProjection(long id) {
        val author = new AuthorDto(id, "author" + id);
        val genre = new GenreDto(id, "genre" + id);
        return new BookProjection(id, "book" + id, author, genre, 0);
    }

    private static BookDto createBookDto(long id) {
        val author = new AuthorDto(id, "author" + id);
        val genre = new GenreDto(id, "genre" + id);
        return new BookDto(id, "book" + id, author, genre, null);
    }

    private static BookProjection toBookProjection(BookDto book) {
        long numberOfComments = ofNullable(book.getComments()).map(List::size).orElse(0);
        return new BookProjection(book.getId(), book.getTitle(), book.getAuthor(),
                book.getGenre(), numberOfComments);
    }
}