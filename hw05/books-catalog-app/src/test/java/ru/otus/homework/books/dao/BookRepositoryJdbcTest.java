package ru.otus.homework.books.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import ru.otus.homework.books.model.Author;
import ru.otus.homework.books.model.Book;
import ru.otus.homework.books.model.Genre;

import static java.util.Comparator.comparing;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.otus.homework.books.dao.AuthorRepositoryJdbcTest.AGATHA_CHRISTIE;
import static ru.otus.homework.books.dao.AuthorRepositoryJdbcTest.ALEXANDR_DUMAS;
import static ru.otus.homework.books.dao.AuthorRepositoryJdbcTest.MARK_TWAIN;
import static ru.otus.homework.books.dao.GenreRepositoryJdbcTest.DETECTIVE;
import static ru.otus.homework.books.dao.GenreRepositoryJdbcTest.HISTORICAL_FICTION;

@DisplayName("Репозиторий на основе Jdbc для работы с книгами")
@JdbcTest
@Import({BookRepositoryJdbc.class, AuthorRepositoryJdbc.class, GenreRepositoryJdbc.class})
public class BookRepositoryJdbcTest {
    public static final int NUMBER_OF_BOOKS = 8;
    public static final int NUMBER_OF_DETECTIVE_BOOKS = 3;
    public static final int NUMBER_OF_TWAIN_BOOKS = 5;
    public static final int NUMBER_OF_JEANNE_D_ARC_BOOKS = 2;
    public static final int NUMBER_OF_TWAIN_DETECTIVES = 1;
    public static final long TWAIN_D_ARK_BOOK_ID = 5;
    public static final String JEANNE_D_ARC = "Жанна д'Арк";
    public static final String THREE_MUSKETEERS = "Три мушкетёра";
    public static final String ADVENTURES_OF_TOM_SAWYER = "Приключения Тома Сойера";
    public static final String TOM_SAWYER_DETECTIVES = "Том Сойер, детектив";

    @Autowired
    private BookRepository bookDao;

    @Autowired
    private AuthorRepository authorDao;

    @Autowired
    private GenreRepository genreDao;

    @DisplayName("Кол-во книг")
    @Test
    void count() {
        var count = bookDao.count();
        assertEquals(count, NUMBER_OF_BOOKS);
    }

    @DisplayName("Поиск всех книг")
    @Test
    void findAll() {
        var books = bookDao.findAll();
        assertThat(books).isNotNull().hasSize(NUMBER_OF_BOOKS)
                .allMatch(s -> s.getName() != null && !s.getName().isBlank(), "Name is not blank")
                .allMatch(s -> s.getAuthor() != null, "Author is presented")
                .allMatch(s -> s.getGenre() != null, "Genre is presented");
        //books.forEach(System.out::println);
    }

    @DisplayName("Поиск по ID")
    @Test
    void findById() {
        var book = bookDao.findById(TWAIN_D_ARK_BOOK_ID);
        assertThat(book).isNotNull()
                .matches(s -> s.getName() != null && !s.getName().isBlank(), "Name is not blank")
                .matches(s -> s.getAuthor() != null, "Author is presented")
                .matches(s -> s.getGenre() != null, "Genre is presented");
        assertEquals(JEANNE_D_ARC, book.getName(), "Book name");
        assertEquals(MARK_TWAIN, book.getAuthor().getName(), "Author name");
        assertEquals(HISTORICAL_FICTION, book.getGenre().getName(), "Genre name");
    }

    @DisplayName("Поиск по альтернативному ключу: название + автор")
    @Test
    void findByNameAndAuthor() {
        var author = getAuthor(MARK_TWAIN);
        var book = bookDao.findByNameAndAuthor(JEANNE_D_ARC, author);
        assertTrue(book.isPresent());
        assertEquals(JEANNE_D_ARC, book.get().getName(), "Book name");
        assertEquals(author, book.get().getAuthor(), "Author name");
        assertEquals(HISTORICAL_FICTION, book.get().getGenre().getName(), "Genre name");
    }

    @DisplayName("Добавить книгу")
    @Test
    void saveNewBook() {
        var author = getAuthor(ALEXANDR_DUMAS);
        var genre = getGenre(HISTORICAL_FICTION);
        var bookToSave = new Book(NUMBER_OF_BOOKS + 1L, THREE_MUSKETEERS, author, genre);
        bookDao.save(bookToSave);

        var book = bookDao.findById(bookToSave.getId());
        assertThat(book).isNotNull()
                .matches(s -> s.getAuthor() != null, "Author is presented")
                .matches(s -> s.getGenre() != null, "Genre is presented");
        assertEquals(THREE_MUSKETEERS, book.getName(), "Book name");
        assertEquals(author, book.getAuthor(), "Author");
        assertEquals(genre, book.getGenre(), "Genre");
    }

    @DisplayName("Добавить книгу с генерируемым ID")
    @Test
    void saveNewBookGeneratedId() {
        var author = getAuthor(ALEXANDR_DUMAS);
        var genre = getGenre(HISTORICAL_FICTION);
        bookDao.save(new Book(null, THREE_MUSKETEERS, author, genre));
        var optionalBook = bookDao.findByNameAndAuthor(THREE_MUSKETEERS, author);
        assertTrue(optionalBook.isPresent());
        var book = optionalBook.get();
        assertEquals(NUMBER_OF_BOOKS + 1L, book.getId(), "Book ID");
        assertEquals(THREE_MUSKETEERS, book.getName(), "Book name");
        assertEquals(author, book.getAuthor(), "Author");
        assertEquals(genre, book.getGenre(), "Genre");
    }

    @DisplayName("Изменить книгу")
    @Test
    void saveModifiedBook() {
        var author = getAuthor(AGATHA_CHRISTIE);
        var genre = getGenre(DETECTIVE);
        var bookName = "Смерть на Ниле";

        var origBook = bookDao.findById(TWAIN_D_ARK_BOOK_ID);
        origBook.setName(bookName);
        origBook.setAuthor(author);
        origBook.setGenre(genre);

        bookDao.save(origBook);

        var book = bookDao.findById(origBook.getId());

        assertThat(book).isNotNull()
                .matches(s -> s.getAuthor() != null, "Author is presented")
                .matches(s -> s.getGenre() != null, "Genre is presented");
        assertEquals(bookName, book.getName(), "Book name");
        assertEquals(author, book.getAuthor(), "Author");
        assertEquals(genre, book.getGenre(), "Genre");
    }

    @DisplayName("Изменить книгу. Ошибка: Две одноимённые книги одного автора")
    @Test
    void saveBookDuplicateKeyException() {
        var book = bookDao.findById(TWAIN_D_ARK_BOOK_ID);
        book.setName(ADVENTURES_OF_TOM_SAWYER);
        assertThrows(DuplicateKeyException.class, () -> bookDao.save(book));
    }

    @DisplayName("Изменить книгу. Автор удалён")
    @Test
    void saveBookDataIntegrityViolationException() {
        var author = getAuthor(AuthorRepositoryJdbcTest.LEO_TOLSTOY);

        var book = bookDao.findById(TWAIN_D_ARK_BOOK_ID);
        book.setAuthor(author);

        authorDao.delete(author);

        assertThrows(DataIntegrityViolationException.class, () -> bookDao.save(book));
    }

    @DisplayName("Поиск книги по названию. Две книги разных авторов")
    @Test
    void findByName() {
        var books = bookDao.findByName(JEANNE_D_ARC);
        assertThat(books).isNotNull().hasSize(2);
        books.sort(comparing(Book::getId));
        var twainsBook = books.get(0);
        assertEquals(JEANNE_D_ARC, twainsBook.getName(), "1st Book name");
        assertEquals(MARK_TWAIN, twainsBook.getAuthor().getName(), "1st Author name");
        assertEquals(HISTORICAL_FICTION, twainsBook.getGenre().getName(), "1st Genre name");
        var dumasBook = books.get(1);
        assertEquals(JEANNE_D_ARC, dumasBook.getName(), "2nd Book name");
        assertEquals(ALEXANDR_DUMAS, dumasBook.getAuthor().getName(), "2nd Author name");
        assertEquals(GenreRepositoryJdbcTest.HISTORICAL_CHRONICLES, dumasBook.getGenre().getName(), "2nd Genre name");
    }

    @DisplayName("Поиск книг по автору")
    @Test
    void findByAuthor() {
        var author = getAuthor(MARK_TWAIN);
        var books = bookDao.findByAuthor(author);
        assertThat(books).isNotNull().hasSize(NUMBER_OF_TWAIN_BOOKS)
                .allMatch(s -> author.equals(s.getAuthor()), "Author")
                .allMatch(s -> s.getName() != null && !s.getName().isBlank(), "Name is not blank")
                .allMatch(s -> s.getGenre() != null, "Genre is presented");
    }

    @DisplayName("Поиск книг по жанру")
    @Test
    void findByGenre() {
        var genre = getGenre(DETECTIVE);
        var books = bookDao.findByGenre(genre);
        assertThat(books).isNotNull().hasSize(NUMBER_OF_DETECTIVE_BOOKS)
                .allMatch(s -> genre.equals(s.getGenre()), "Genre")
                .allMatch(s -> s.getAuthor() != null, "Author is presented")
                .allMatch(s -> s.getName() != null && !s.getName().isBlank(), "Name is not blank");
    }

    @DisplayName("Удалить книгу по ID")
    @Test
    void deleteById() {
        bookDao.deleteById(TWAIN_D_ARK_BOOK_ID);
        assertThrows(EmptyResultDataAccessException.class, () -> bookDao.findById(TWAIN_D_ARK_BOOK_ID));
    }

    @DisplayName("Удалить книгу")
    @Test
    void delete() {
        var book = bookDao.findById(TWAIN_D_ARK_BOOK_ID);
        bookDao.delete(book);
        assertThrows(EmptyResultDataAccessException.class, () -> bookDao.findById(TWAIN_D_ARK_BOOK_ID));
    }

    @DisplayName("Удалить книги определённого автора")
    @Test
    void deleteAllByAuthor() {
        var author = getAuthor(MARK_TWAIN);
        bookDao.deleteAllByAuthor(author);
        assertEquals(NUMBER_OF_BOOKS - NUMBER_OF_TWAIN_BOOKS, bookDao.count());
    }

    @DisplayName("Удалить все книги")
    @Test
    void deleteAll() {
        authorDao.findAllUsed().forEach(bookDao::deleteAllByAuthor);
        assertEquals(0, bookDao.count());
    }

    @DisplayName("Удалить книги определённого жанра")
    @Test
    void deleteAllByGenre() {
        var genre = getGenre(DETECTIVE);
        bookDao.deleteAllByGenre(genre);
        assertEquals(NUMBER_OF_BOOKS - NUMBER_OF_DETECTIVE_BOOKS, bookDao.count());
    }

    private Author getAuthor(String name) {
        var author = authorDao.findByName(name);
        assertTrue(author.isPresent());
        return author.get();
    }

    private Genre getGenre(String name) {
        var genre = genreDao.findByName(name);
        assertTrue(genre.isPresent());
        return genre.get();
    }

}
