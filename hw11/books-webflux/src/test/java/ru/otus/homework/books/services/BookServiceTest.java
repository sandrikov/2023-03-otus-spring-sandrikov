package ru.otus.homework.books.services;

import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.otus.homework.books.rest.dto.BookDto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.otus.homework.books.misc.TestConstants.ADVENTURES_OF_TOM_SAWYER;
import static ru.otus.homework.books.misc.TestConstants.ADVENTURES_OF_TOM_SAWYER_ID;
import static ru.otus.homework.books.misc.TestConstants.AGATHA_CHRISTIE;
import static ru.otus.homework.books.misc.TestConstants.AGATHA_CHRISTIE_ID;
import static ru.otus.homework.books.misc.TestConstants.ALEXANDR_DUMAS;
import static ru.otus.homework.books.misc.TestConstants.ALEXANDR_DUMAS_ID;
import static ru.otus.homework.books.misc.TestConstants.DETECTIVE;
import static ru.otus.homework.books.misc.TestConstants.DETECTIVE_GENRE_ID;
import static ru.otus.homework.books.misc.TestConstants.DUMA_D_ARK_BOOK_ID;
import static ru.otus.homework.books.misc.TestConstants.HISTORICAL_FICTION;
import static ru.otus.homework.books.misc.TestConstants.HISTORICAL_FICTION_ID;
import static ru.otus.homework.books.misc.TestConstants.JEANNE_D_ARC;
import static ru.otus.homework.books.misc.TestConstants.MARK_TWAIN;
import static ru.otus.homework.books.misc.TestConstants.NUMBER_OF_BOOKS;
import static ru.otus.homework.books.misc.TestConstants.THREE_MUSKETEERS;
import static ru.otus.homework.books.misc.TestConstants.TWAIN_D_ARK_BOOK_ID;

@DisplayName("API для работы с книгами. Интеграционный тест")
@SpringBootTest
class BookServiceTest {

    @Autowired
    private AuthorService authorService;
    @Autowired
    private GenreService genreService;
    @Autowired
    private BookService bookService;

    @DisplayName("Поиск всех книг")
    @Test
    void listBooks() {
        StepVerifier.create(bookService.listBooks())
                .expectNextCount(NUMBER_OF_BOOKS)
                .verifyComplete();
    }

    @DisplayName("Добавить книгу")
    @Test
    void createBook() {
        val bookMono = Mono
                .just(new BookDto().setTitle(THREE_MUSKETEERS))
                .zipWith(authorService.getAuthor(ALEXANDR_DUMAS_ID), BookDto::setAuthor)
                .zipWith(genreService.getGenre(HISTORICAL_FICTION_ID), BookDto::setGenre);

        StepVerifier.create(bookService.createBook(bookMono))
                .assertNext(book -> {
                    assertEquals(THREE_MUSKETEERS, book.getTitle(), "Book title");
                    assertEquals(ALEXANDR_DUMAS, book.getAuthor().getName(), "Author name");
                    assertEquals(HISTORICAL_FICTION, book.getGenre().getName(), "Genre name");
                })
                .verifyComplete();
    }

    @DisplayName("Поиск книги по ID")
    @Test
    void getBook() {
        StepVerifier.create(bookService.getBook(TWAIN_D_ARK_BOOK_ID))
                .assertNext(book -> {
                    assertEquals(JEANNE_D_ARC, book.getTitle(), "Book title");
                    assertEquals(MARK_TWAIN, book.getAuthor().getName(), "Author name");
                    assertEquals(HISTORICAL_FICTION, book.getGenre().getName(), "Genre name");
                })
                .verifyComplete();
    }

    @DisplayName("Изменить книгу")
    @Test
    void modifyBook() {
        val bookTitle = "Смерть на Ниле";
        val bookMono = Mono
                .just(new BookDto().setId(DUMA_D_ARK_BOOK_ID).setTitle(bookTitle))
                .zipWith(authorService.getAuthor(AGATHA_CHRISTIE_ID), BookDto::setAuthor)
                .zipWith(genreService.getGenre(DETECTIVE_GENRE_ID), BookDto::setGenre);
        StepVerifier.create(bookService.updateBook(DUMA_D_ARK_BOOK_ID, bookMono))
                .assertNext(book -> {
                    assertEquals(bookTitle, book.getTitle(), "Book title");
                    assertEquals(AGATHA_CHRISTIE, book.getAuthor().getName(), "Author name");
                    assertEquals(DETECTIVE, book.getGenre().getName(), "Genre name");
                })
                .verifyComplete();
    }

    @DisplayName("Удалить книгу")
    @Test
    void deleteBookAndCheck() {
        StepVerifier.create(bookService.deleteBook(ADVENTURES_OF_TOM_SAWYER_ID))
                .assertNext(book -> assertEquals(ADVENTURES_OF_TOM_SAWYER, book.getTitle(), "Book title"))
                .verifyComplete();
        StepVerifier.create(bookService.getBook(ADVENTURES_OF_TOM_SAWYER_ID))
                .expectNextCount(0).verifyComplete();
    }

}