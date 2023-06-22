package ru.otus.homework.books.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.homework.books.rest.dto.AuthorDto;
import ru.otus.homework.books.rest.dto.BookDto;
import ru.otus.homework.books.rest.dto.BookProjection;
import ru.otus.homework.books.rest.dto.GenreDto;
import ru.otus.homework.books.rest.misc.BookAppAdvice;
import ru.otus.homework.books.services.BookService;

import java.util.List;

import static java.util.Optional.ofNullable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.otus.homework.books.rest.misc.RestErrorMessages.getNewEntityHaveIdMessage;
import static ru.otus.homework.books.services.ServiceResponse.done;
import static ru.otus.homework.books.services.ServiceResponse.error;
import static ru.otus.homework.books.services.misc.ServiceErrorMessages.getBookNotFoundMessage;

@WebMvcTest(BookRestController.class)
class BookRestControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private BookService bookService;
    private static final String ENTITY_API_URL = "/api/books";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";


    @Test
    void getAllBooks() throws Exception {
        val books = List.of(createBookProjection(1), createBookProjection(2));
        given(bookService
                .listBooks(null, null, null))
                .willReturn(done(books));
        mvc.perform(get(ENTITY_API_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(mapper.writeValueAsString(books)));
    }

    @Test
    void createBook() throws Exception {
        val bookToCreate = createBookDto(1);
        bookToCreate.setId(0);
        val createdBook = createBookDto(1);
        given(bookService
                .createBook(any(), any(), any()))
                .willReturn(done(createdBook));
        mvc.perform(post(ENTITY_API_URL)
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(bookToCreate)))
                .andExpect(status().isCreated())
                .andExpect(content().json(mapper.writeValueAsString(createdBook)));
    }

    @Test
    void createBookWithExistingId() throws Exception {
        val bookToCreate = createBookDto(1);
        val message = getNewEntityHaveIdMessage("book");
        mvc.perform(post(ENTITY_API_URL)
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(bookToCreate)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().json(expectedErrorBody(message)));
    }

    @Test
    void putExistingBook() throws Exception {
        val book = createBookDto(1);
        given(bookService.modifyBook(any())).willReturn(done(book));
        val json = mapper.writeValueAsString(book);
        mvc.perform(put(ENTITY_API_URL_ID, book.getId())
                    .contentType(APPLICATION_JSON)
                    .content(json))
                .andExpect(status().isOk())
                .andExpect(content().json(json));
    }

    @Test
    void putNonExistingBook() throws Exception {
        val book = createBookDto(1);
        val message = getBookNotFoundMessage(1);
        given(bookService.modifyBook(any())).willReturn(error(message));
        mvc.perform(put(ENTITY_API_URL_ID, book.getId())
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(book)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().json(expectedErrorBody(message)));
    }

    @Test
    void getBook() throws Exception {
        val book = createBookDto(1);
        given(bookService.getBook(book.getId())).willReturn(done(book));
        mvc.perform(get(ENTITY_API_URL_ID, book.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(book.getId()))
            .andExpect(jsonPath("$.title").value(book.getTitle()))
            .andExpect(jsonPath("$.author.id").value(book.getAuthor().getId()))
            .andExpect(jsonPath("$.author.name").value(book.getAuthor().getName()))
            .andExpect(jsonPath("$.genre.id").value(book.getGenre().getId()))
            .andExpect(jsonPath("$.genre.name").value(book.getGenre().getName()));
    }

    @Test
    void getNonExistingBook() throws Exception {
        given(bookService.getBook(Long.MAX_VALUE))
                .willReturn(error(getBookNotFoundMessage(Long.MAX_VALUE)));
        mvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteBook() throws Exception {
        val book = createBookDto(1);
        given(bookService.deleteBook(book.getId()))
                .willReturn(done(toBookProjection(book)));
        mvc.perform(delete(ENTITY_API_URL_ID, book.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void fullUpdateBookWithPatch() throws Exception {
        val book = createBookDto(1);
        given(bookService.modifyBook(any())).willReturn(done(book));
        val json = mapper.writeValueAsString(book);
        mvc.perform(patch(ENTITY_API_URL_ID, book.getId())
                        .contentType("application/merge-patch+json")
                        .content(mapper.writeValueAsBytes(book)))
                .andExpect(status().isOk())
                .andExpect(content().json(json));
    }

    @Test
    void patchNonExistingBook() throws Exception {
        val message = getBookNotFoundMessage(99);
        val book = createBookDto(99);
        given(bookService.modifyBook(any())).willReturn(error(message));
        mvc.perform(patch(ENTITY_API_URL_ID, book.getId())
                        .contentType("application/merge-patch+json")
                        .content(mapper.writeValueAsBytes(book)))
                .andExpect(status().isNotFound());
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

    private String expectedErrorBody(String expectedErrorMessage) throws JsonProcessingException {
        return mapper.writeValueAsString(new BookAppAdvice.Response(expectedErrorMessage));
    }

}