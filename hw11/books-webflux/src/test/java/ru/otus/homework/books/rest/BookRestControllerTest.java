package ru.otus.homework.books.rest;

import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.sql.init.SqlR2dbcScriptDatabaseInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.homework.books.rest.dto.AuthorDto;
import ru.otus.homework.books.rest.dto.BookDto;
import ru.otus.homework.books.rest.dto.GenreDto;
import ru.otus.homework.books.services.BookService;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@Log4j2
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@MockBean(classes = {SqlR2dbcScriptDatabaseInitializer.class})
class BookRestControllerTest {

    private static final String ENTITY_API_URL = "/api/books";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @MockBean
    private BookService bookService;

    @LocalServerPort
    private int port;

    private WebTestClient client;

    @BeforeEach
    void setup() {
        this.client = WebTestClient.bindToServer().baseUrl("http://localhost:" + port).build();
    }

    @Test
    void getBook() {
        val book = createBookDto(1);
        given(bookService.getBook(book.getId())).willReturn(Mono.just(book));

        client.get().uri(ENTITY_API_URL_ID, book.getId())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(BookDto.class)
                .value(result -> assertEquals(book, result));
    }

    @Test
    void getAllBooks() {
        val books = new BookDto[] {createBookDto(1), createBookDto(2)};
        given(bookService.listBooks()).willReturn(Flux.just(books));

        client.get().uri(ENTITY_API_URL)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(BookDto.class)
                .value(result -> assertArrayEquals(books, result.toArray()));
    }

    @Test
    void createBook() {
        val bookToCreate = createBookDto(1);
        bookToCreate.setId(null);
        val createdBook = createBookDto(1);
        given(bookService.createBook(any())).willReturn(Mono.just(createdBook));

        client.post().uri(ENTITY_API_URL)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(bookToCreate)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(BookDto.class)
                .value(result -> assertEquals(createdBook, result));
    }

    @Test
    void putExistingBook() {
        val book = createBookDto(1);
        given(bookService.updateBook(any(), any())).willReturn(Mono.just(book));

        client.put().uri(ENTITY_API_URL_ID, book.getId())
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(book)
                .exchange()
                .expectStatus().isOk()
                .expectBody(BookDto.class)
                .value(result -> assertEquals(book, result));
    }

    @Test
    void putNonExistingBook() {
        val book = createBookDto(1);
        given(bookService.updateBook(any(), any())).willReturn(Mono.empty());

        client.put().uri(ENTITY_API_URL_ID, book.getId())
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(book)
                .exchange()
                .expectStatus().isNotFound();
    }


    @Test
    void deleteBook() {
        val book = createBookDto(1);
        given(bookService.deleteBook(book.getId())).willReturn(Mono.just(book));

        client.delete().uri(ENTITY_API_URL_ID, book.getId())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNoContent();
    }

    private static BookDto createBookDto(long id) {
        val author = new AuthorDto(id, "author" + id);
        val genre = new GenreDto(id, "genre" + id);
        return new BookDto(id, "book" + id, author, genre);
    }

}