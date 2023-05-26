package ru.otus.homework.books.shell;

import jakarta.validation.constraints.Size;
import org.apache.commons.text.TextStringBuilder;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.homework.books.model.Book;
import ru.otus.homework.books.services.BookService;

import java.util.List;
import java.util.function.Function;

import static java.util.Comparator.comparing;
import static org.springframework.shell.standard.ShellOption.NULL;
import static ru.otus.homework.books.services.ServiceResponse.Status.ERROR;

@ShellComponent
public class BooksCommands {

    public static final String BOOKS_FOUND = "%d books found.";

    public static final String BOOK_DELETED = "Delete book is done.";

    public static final String BOOK_ADDED = "Add book is done.";

    public static final String BOOK_UPDATED = "Modify book is done.";

    public static final String BOOKS_DELETED = "%d books deleted.";

    public static final String[] TABLE_HEADER_ROW = {"Author", "Genre", "Name", "ID"};

    private static final String COMMAND_GROUP_BOOKS = "Books";

    private static final int MIN_NAME_LEN = 2;

    private static final int MAX_NAME_LEN = 255;

    private final BookService bookService;

    private final ShellHelper shellHelper;

    public BooksCommands(BookService bookService, ShellHelper shellHelper) {
        this.bookService = bookService;
        this.shellHelper = shellHelper;
    }

    @ShellMethod(value = "Lists books", key = {"list-books", "lb"}, group = COMMAND_GROUP_BOOKS)
    public String listBooks(@ShellOption(value = {"-A", "--author-id"}, defaultValue = NULL) Long authorId,
                            @ShellOption(value = {"-G", "--genre-id"}, defaultValue = NULL) Long genreId,
                            @ShellOption(value = {"-N", "--name"}, defaultValue = NULL) String name) {
        var response = bookService.listBooks(authorId, genreId, name);
        if (response.getStatus() == ERROR) {
            return shellHelper.getErrorMessage(response.getMessage());
        }
        var books = response.getData();
        var report = String.format(BOOKS_FOUND, books.size());
        if (books.isEmpty()) {
            return shellHelper.getWarningMessage(report);
        }
        var table = createTable(books);
        return table.concat(shellHelper.getSuccessMessage(report));
    }

    @ShellMethod(value = "Get book by ID", key = {"get-book", "gb"}, group = COMMAND_GROUP_BOOKS)
    public String getBook(@ShellOption(value = {"-I", "--id"}) Long id) {
        var response = bookService.getBook(id);
        if (response.getStatus() == ERROR) {
            return shellHelper.getErrorMessage(response.getMessage());
        }
        return printSuccess(response.getData(), String.format(BOOKS_FOUND, 1));
    }

    @ShellMethod(value = "Add book", key = {"add-book", "ab"}, group = COMMAND_GROUP_BOOKS)
    public String createBook(@ShellOption(value = {"-N", "--name"})
                             @Size(min = MIN_NAME_LEN, max = MAX_NAME_LEN) String name,
                             @ShellOption(value = {"-A", "--author-id"}) Long authorId,
                             @ShellOption(value = {"-G", "--genre-id"}) Long genreId) {
        var response = bookService.createBook(name, authorId, genreId);
        if (response.getStatus() == ERROR) {
            return shellHelper.getErrorMessage(response.getMessage());
        }
        return printSuccess(response.getData(), BOOK_ADDED);
    }

    @ShellMethod(value = "Modify book", key = {"modify-book", "mb"}, group = COMMAND_GROUP_BOOKS)
    public String modifyBook(@ShellOption(value = {"-I", "--id"}) Long id,
                             @ShellOption(value = {"-N", "--name"}, defaultValue = NULL)
                             @Size(min = MIN_NAME_LEN, max = MAX_NAME_LEN) String name,
                             @ShellOption(value = {"-A", "--author-id"}, defaultValue = NULL) Long authorId,
                             @ShellOption(value = {"-G", "--genre-id"}, defaultValue = NULL) Long genreId) {
        if (name == null && authorId == null && genreId == null) {
            return shellHelper.getErrorMessage("Use at least one option: name or author or genre");
        }
        var response = bookService.modifyBook(id, name, authorId, genreId);
        if (response.getStatus() == ERROR) {
            return shellHelper.getErrorMessage(response.getMessage());
        }
        return printSuccess(response.getData(), BOOK_UPDATED);
    }

    @ShellMethod(value = "Delete book by ID", key = {"delete-book", "db"}, group = COMMAND_GROUP_BOOKS)
    public String deleteBook(@ShellOption(value = {"-I", "--id"}) Long id) {
        var response = bookService.deleteBook(id);
        if (response.getStatus() == ERROR) {
            return shellHelper.getErrorMessage(response.getMessage());
        }
        return printSuccess(response.getData(), BOOK_DELETED);
    }

    @ShellMethod(value = "Delete books by name or author or genre",
            key = {"delete-books", "dbs"}, group = COMMAND_GROUP_BOOKS)
    public String deleteBooks(@ShellOption(value = {"-A", "--author-id"}, defaultValue = NULL) Long authorId,
                              @ShellOption(value = {"-G", "--genre-id"}, defaultValue = NULL) Long genreId) {
        var response = bookService.deleteBooks(authorId, genreId);
        if (response.getStatus() == ERROR) {
            return shellHelper.getErrorMessage(response.getMessage());
        }
        int count = response.getData();
        var message = String.format(BOOKS_DELETED, count);
        return count == 0 ? shellHelper.getWarningMessage(message) : shellHelper.getSuccessMessage(message);
    }

    private String format(Book book) {
        return shellHelper.getInfoMessage(String.format("%s \"%s\", %s, id=%d",
                book.getAuthor().getName(), book.getName(), book.getGenre().getName(), book.getId()));
    }

    private String printSuccess(Book book, String report) {
        return new TextStringBuilder().append(format(book)).appendNewLine()
                .append(shellHelper.getSuccessMessage(report)).toString();
    }

    private String createTable(List<Book> books) {
        var comparator = comparing((Book b) -> b.getAuthor().getName())
                .thenComparing((Book b) -> b.getGenre().getName())
                .thenComparing(Book::getName);
        Function<Book, Object[]> mapper = b -> new Object[]{
                b.getAuthor().getName(), b.getGenre().getName(), b.getName(), b.getId()};
        return shellHelper.createTable(books, comparator, mapper, TABLE_HEADER_ROW);
    }
}
