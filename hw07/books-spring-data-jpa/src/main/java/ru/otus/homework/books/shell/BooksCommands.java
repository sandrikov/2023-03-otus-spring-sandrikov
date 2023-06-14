package ru.otus.homework.books.shell;

import jakarta.validation.constraints.Size;
import org.apache.commons.text.TextStringBuilder;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.homework.books.dto.BookProjection;
import ru.otus.homework.books.services.BookService;

import java.util.List;
import java.util.function.Function;

import static java.util.Comparator.comparing;
import static org.springframework.shell.standard.ShellOption.NULL;
import static ru.otus.homework.books.domain.SchemaSqlConstants.MAX_TITLE_LENGTH;
import static ru.otus.homework.books.services.ServiceResponse.Status.ERROR;

@ShellComponent
@ShellCommandGroup("Books")
public class BooksCommands {

    public static final String BOOKS_FOUND = "%d books found.";

    public static final String BOOK_DELETED = "Delete book is done.";

    public static final String BOOK_ADDED = "Add book is done.";

    public static final String BOOK_UPDATED = "Modify book is done.";

    public static final String BOOKS_DELETED = "%d books deleted.";

    public static final String[] TABLE_HEADER_ROW = {"Author", "Genre", "Title", "ID", "Comments"};

    private static final int MIN_TITLE_LEN = 2;

    private final BookService bookService;

    private final ShellHelper shellHelper;

    public BooksCommands(BookService bookService, ShellHelper shellHelper) {
        this.bookService = bookService;
        this.shellHelper = shellHelper;
    }

    @ShellMethod(value = "Lists books", key = {"list books", "lb"})
    public String listBooks(@ShellOption(value = {"-A", "--author-id"}, defaultValue = NULL) Long authorId,
                            @ShellOption(value = {"-G", "--genre-id"}, defaultValue = NULL) Long genreId,
                            @ShellOption(value = {"-T", "--title"}, defaultValue = NULL) String title) {
        var response = bookService.listBooks(authorId, genreId, title);
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

    @ShellMethod(value = "Get book by ID", key = {"get book", "gb"})
    public String getBook(@ShellOption(value = {"-I", "--id"}) Long id) {
        var response = bookService.getBookProjection(id);
        if (response.getStatus() == ERROR) {
            return shellHelper.getErrorMessage(response.getMessage());
        }
        return printSuccess(response.getData(), String.format(BOOKS_FOUND, 1));
    }

    @ShellMethod(value = "Add book", key = {"add book", "ab"})
    public String createBook(@ShellOption(value = {"-T", "--title"})
                             @Size(min = MIN_TITLE_LEN, max = MAX_TITLE_LENGTH) String title,
                             @ShellOption(value = {"-A", "--author-id"}) Long authorId,
                             @ShellOption(value = {"-G", "--genre-id"}) Long genreId) {
        var response = bookService.createBook(title, authorId, genreId);
        if (response.getStatus() == ERROR) {
            return shellHelper.getErrorMessage(response.getMessage());
        }
        return printSuccess(response.getData(), BOOK_ADDED);
    }

    @ShellMethod(value = "Modify book", key = {"modify book", "mb"})
    public String modifyBook(@ShellOption(value = {"-I", "--id"}) Long id,
                             @ShellOption(value = {"-T", "--title"}, defaultValue = NULL)
                             @Size(min = MIN_TITLE_LEN, max = MAX_TITLE_LENGTH) String title,
                             @ShellOption(value = {"-A", "--author-id"}, defaultValue = NULL) Long authorId,
                             @ShellOption(value = {"-G", "--genre-id"}, defaultValue = NULL) Long genreId) {
        if (title == null && authorId == null && genreId == null) {
            return shellHelper.getErrorMessage("Use at least one option: title or author or genre");
        }
        var response = bookService.modifyBook(id, title, authorId, genreId);
        if (response.getStatus() == ERROR) {
            return shellHelper.getErrorMessage(response.getMessage());
        }
        return printSuccess(response.getData(), BOOK_UPDATED);
    }

    @ShellMethod(value = "Delete book by ID", key = {"delete book", "db"})
    public String deleteBook(@ShellOption(value = {"-I", "--id"}) Long id) {
        var response = bookService.deleteBook(id);
        if (response.getStatus() == ERROR) {
            return shellHelper.getErrorMessage(response.getMessage());
        }
        return printSuccess(response.getData(), BOOK_DELETED);
    }

    @ShellMethod(value = "Delete books by title or author or genre",
            key = {"delete books", "dbs"})
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

    private String format(BookProjection book) {
        return shellHelper.getInfoMessage(String.format("%s \"%s\", %s, id=%d",
                book.author().getName(), book.title(), book.title(), book.id()));
    }

    private String printSuccess(BookProjection book, String report) {
        return new TextStringBuilder().append(format(book)).appendNewLine()
                .append(shellHelper.getSuccessMessage(report)).toString();
    }

    private String createTable(List<BookProjection> books) {
        var comparator = comparing((BookProjection b) -> b.author().getName())
                .thenComparing((BookProjection b) -> b.genre().getName())
                .thenComparing(BookProjection::title);
        Function<BookProjection, Object[]> mapper = b -> new Object[]{
                b.author().getName(), b.genre().getName(), b.title(), b.id(), b.commentCount()};
        return shellHelper.createTable(books, comparator, mapper, TABLE_HEADER_ROW);
    }
}
