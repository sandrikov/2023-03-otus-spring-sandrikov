package ru.otus.homework.books.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.otus.homework.books.dao.AuthorRepositoryJdbc;
import ru.otus.homework.books.dao.BookRepositoryJdbc;
import ru.otus.homework.books.dao.GenreRepositoryJdbc;
import ru.otus.homework.books.model.Author;
import ru.otus.homework.books.model.Genre;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static ru.otus.homework.books.dao.AuthorRepositoryJdbcTest.AGATHA_CHRISTIE;
import static ru.otus.homework.books.dao.AuthorRepositoryJdbcTest.ALEXANDR_DUMAS;
import static ru.otus.homework.books.dao.AuthorRepositoryJdbcTest.LEO_TOLSTOY;
import static ru.otus.homework.books.dao.AuthorRepositoryJdbcTest.MARK_TWAIN;
import static ru.otus.homework.books.dao.BookRepositoryJdbcTest.ADVENTURES_OF_TOM_SAWYER;
import static ru.otus.homework.books.dao.BookRepositoryJdbcTest.JEANNE_D_ARC;
import static ru.otus.homework.books.dao.BookRepositoryJdbcTest.NUMBER_OF_BOOKS;
import static ru.otus.homework.books.dao.BookRepositoryJdbcTest.NUMBER_OF_DETECTIVE_BOOKS;
import static ru.otus.homework.books.dao.BookRepositoryJdbcTest.NUMBER_OF_JEANNE_D_ARC_BOOKS;
import static ru.otus.homework.books.dao.BookRepositoryJdbcTest.NUMBER_OF_TWAIN_BOOKS;
import static ru.otus.homework.books.dao.BookRepositoryJdbcTest.NUMBER_OF_TWAIN_DETECTIVES;
import static ru.otus.homework.books.dao.BookRepositoryJdbcTest.THREE_MUSKETEERS;
import static ru.otus.homework.books.dao.BookRepositoryJdbcTest.TOM_SAWYER_DETECTIVES;
import static ru.otus.homework.books.dao.BookRepositoryJdbcTest.TWAIN_D_ARK_BOOK_ID;
import static ru.otus.homework.books.dao.GenreRepositoryJdbcTest.AUTOBIOGRAPHY;
import static ru.otus.homework.books.dao.GenreRepositoryJdbcTest.DETECTIVE;
import static ru.otus.homework.books.dao.GenreRepositoryJdbcTest.HISTORICAL_FICTION;
import static ru.otus.homework.books.services.BookServiceImpl.BOOK_ALREADY_EXISTS;
import static ru.otus.homework.books.services.BookServiceImpl.BOOK_NOT_FOUND;
import static ru.otus.homework.books.services.ServiceResponse.Status.ERROR;
import static ru.otus.homework.books.services.ServiceResponse.Status.OK;

@DisplayName("API для работы с книгами")
@JdbcTest
@Import({BookRepositoryJdbc.class, AuthorRepositoryJdbc.class, GenreRepositoryJdbc.class,
        BookServiceImpl.class, AuthorServiceImpl.class, GenreServiceImpl.class})
class BookServiceTest {

    @Autowired
    private AuthorService authorService;
    @Autowired
    private GenreService genreService;
    @Autowired
    private BookService bookService;

    @DisplayName("Поиск всех книг по имени, автору и жанру: разные комбинации параметров")
    @Test
    void listBooks() {
        checkListBox(NUMBER_OF_BOOKS, null, null, null);
        checkListBox(NUMBER_OF_TWAIN_BOOKS, getAuthor(MARK_TWAIN), null, null);
        checkListBox(NUMBER_OF_DETECTIVE_BOOKS, null, getGenre(DETECTIVE), null);
        checkListBox(NUMBER_OF_JEANNE_D_ARC_BOOKS, null, null, JEANNE_D_ARC);
        checkListBox(NUMBER_OF_TWAIN_DETECTIVES, getAuthor(MARK_TWAIN), getGenre(DETECTIVE), null);
        checkListBox(NUMBER_OF_TWAIN_DETECTIVES, getAuthor(MARK_TWAIN), getGenre(DETECTIVE), TOM_SAWYER_DETECTIVES);
        checkListBox(0, getAuthor(AGATHA_CHRISTIE), null, JEANNE_D_ARC);
        checkListBox(NUMBER_OF_JEANNE_D_ARC_BOOKS, null, null, JEANNE_D_ARC);
    }

    private void checkListBox(int expectedSize, Author author, Genre genre, String name) {
        var authorId = author != null ? author.getId() : null;
        var genreId = genre != null ? genre.getId() : null;
        var books = assertOK(bookService.listBooks(authorId, genreId, name));
        assertEquals(expectedSize, books.size());
    }

    @DisplayName("Поиск книги по ID: позитивный и негативный сценарии")
    @Test
    void getBook() {
        var book = assertOK(bookService.getBook(TWAIN_D_ARK_BOOK_ID));
        assertEquals(JEANNE_D_ARC, book.getName(), "Book name");
        assertEquals(MARK_TWAIN, book.getAuthor().getName(), "Author name");
        assertEquals(HISTORICAL_FICTION, book.getGenre().getName(), "Genre name");
        // negative
        assertError(bookService.getBook(99L), BOOK_NOT_FOUND, "id=99");
    }

    @DisplayName("Добавить книгу")
    @Test
    void createBook() {
        var author = getAuthor(ALEXANDR_DUMAS);
        var genre = getGenre(HISTORICAL_FICTION);
        var name = THREE_MUSKETEERS;
        var newBook = assertOK(bookService.createBook(name, author.getId(), genre.getId()));
        var book = assertOK(bookService.getBook(newBook.getId()));
        assertThat(book).isNotNull()
                .matches(s -> s.getAuthor() != null, "Author is presented")
                .matches(s -> s.getGenre() != null, "Genre is presented");
        assertEquals(name, book.getName(), "Book name");
        assertEquals(author, book.getAuthor(), "Author");
        assertEquals(genre, book.getGenre(), "Genre");
    }

    @DisplayName("Добавить книгу. Ошибка: Две одноимённые книги одного автора")
    @Test
    void createBookDuplicateAlternativeKey() {
        var author = getAuthor(ALEXANDR_DUMAS);
        var genre = getGenre(DETECTIVE);
        assertError(bookService.createBook(JEANNE_D_ARC, author.getId(), genre.getId()),
                BOOK_ALREADY_EXISTS, ALEXANDR_DUMAS, JEANNE_D_ARC);
    }

    @DisplayName("Изменить книгу")
    @Test
    void modifyBook() {
        var author = getAuthor(AGATHA_CHRISTIE);
        var genre = getGenre(DETECTIVE);
        var bookName = "Смерть на Ниле";
        assertOK(bookService.modifyBook(TWAIN_D_ARK_BOOK_ID, bookName, author.getId(), genre.getId()));
        var book = assertOK(bookService.getBook(TWAIN_D_ARK_BOOK_ID));
        assertEquals(bookName, book.getName(), "Book name");
        assertEquals(author, book.getAuthor(), "Author");
        assertEquals(genre, book.getGenre(), "Genre");
    }

    @DisplayName("Изменить книгу. Ошибка: Две одноимённые книги одного автора")
    @Test
    void modifyBookDuplicateAlternativeKey() {
        assertError(bookService.modifyBook(TWAIN_D_ARK_BOOK_ID, ADVENTURES_OF_TOM_SAWYER, null, null),
                BOOK_ALREADY_EXISTS, MARK_TWAIN, ADVENTURES_OF_TOM_SAWYER);
   }

    @DisplayName("Удалить книгу: позитивный и негативный сценарии")
    @Test
    void deleteBook() {
        assertOK(bookService.deleteBook(TWAIN_D_ARK_BOOK_ID));
        assertError(bookService.getBook(TWAIN_D_ARK_BOOK_ID), BOOK_NOT_FOUND, "id=" + TWAIN_D_ARK_BOOK_ID);
        // negative
        assertError(bookService.deleteBook(99L), BOOK_NOT_FOUND, "id=99");
    }

    @DisplayName("Удалить все книги")
    @Test
    void deleteAllBooks() {
        checkDeleteBooks(NUMBER_OF_BOOKS, null, null);
    }

    @DisplayName("Удалить книги Марка Твена")
    @Test
    void deleteTwainBooks() {
        checkDeleteBooks(NUMBER_OF_TWAIN_BOOKS, getAuthor(MARK_TWAIN), null);
    }

    @DisplayName("Удалить все детективы")
    @Test
    void deleteDetectives() {
        checkDeleteBooks(NUMBER_OF_DETECTIVE_BOOKS, null, getGenre(DETECTIVE));
    }

    @DisplayName("Удалить детективы Марка Твена")
    @Test
    void deleteTwainDetectives() {
        checkDeleteBooks(NUMBER_OF_TWAIN_DETECTIVES, getAuthor(MARK_TWAIN), getGenre(DETECTIVE));
    }

    @DisplayName("Удалить книги Льва Толстого (нет в каталоге)")
    @Test
    void deleteTolstoyBooks() {
        checkDeleteBooks(0, getAuthor(LEO_TOLSTOY), null);
    }

    @DisplayName("Удалить автобиографии (нет в каталоге)")
    @Test
    void deleteAutobiography() {
        checkDeleteBooks(0, null, getGenre(AUTOBIOGRAPHY));
    }

    private void checkDeleteBooks(int expectedCount, Author author, Genre genre) {
        var authorId = author != null ? author.getId() : null;
        var genreId = genre != null ? genre.getId() : null;
        var deleted = assertOK(bookService.deleteBooks(authorId, genreId));
        var rest = assertOK(bookService.listBooks(null, null, null));
        assertEquals(expectedCount, deleted, "Кол-во удалённых");
        assertEquals(NUMBER_OF_BOOKS - expectedCount, rest.size(), "Кол-во оставшихся");
    }

    private static void assertError(ServiceResponse<?> response, String format, Object... args) {
        assertError(response, String.format(format, args));
    }

    private static void assertError(ServiceResponse<?> response, String expectedMessage) {
        assertEquals(expectedMessage, assertError(response));
    }

    private static String assertError(ServiceResponse<?> response) {
        assertSame(ERROR, response.getStatus());
        return response.getMessage();
    }

    private static <T> T assertOK(ServiceResponse<T> response) {
        assertSame(OK, response.getStatus());
        return response.getData();
    }

    private Author getAuthor(String name) {
        return assertOK(authorService.findAuthor(name));
    }

    private Genre getGenre(String name) {
        return assertOK(genreService.findGenre(name));
    }
}