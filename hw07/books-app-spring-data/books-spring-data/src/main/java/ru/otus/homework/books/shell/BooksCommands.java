package ru.otus.homework.books.shell;

import jakarta.validation.constraints.Size;
import lombok.val;
import org.apache.commons.text.TextStringBuilder;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.homework.books.dto.BookDto;
import ru.otus.homework.books.dto.BookProjection;
import ru.otus.homework.books.dto.CommentDto;
import ru.otus.homework.books.services.BookService;

import java.util.List;
import java.util.function.Function;

import static java.util.Comparator.comparing;
import static java.util.Comparator.comparingLong;
import static org.springframework.shell.standard.ShellOption.NULL;
import static ru.otus.homework.books.domain.Book.MAX_TITLE_LENGTH;
import static ru.otus.homework.books.domain.Comment.MAX_TEXT_LENGTH;
import static ru.otus.homework.books.services.ServiceResponse.Status.ERROR;

@ShellComponent
@ShellCommandGroup("Books")
public class BooksCommands {

    public static final String BOOKS_FOUND = "%d books found.";

    public static final String BOOK_DELETED = "Delete book is done.";

    public static final String BOOK_ADDED = "Add book is done.";

    public static final String BOOK_UPDATED = "Modify book is done.";

    public static final String BOOKS_DELETED = "%d books deleted.";

    public static final String COMMENTS_FOUND = "%d comments found.";

    public static final String COMMENT_DELETED = "Delete comment is done.";

    public static final String COMMENT_ADDED = "Add comment is done.";

    public static final String COMMENT_UPDATED = "Modify comment is done.";

    public static final String[] TABLE_HEADER_ROW = {"Author", "Genre", "Name", "ID", "Comments"};

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
                            @ShellOption(value = {"-T", "--title"}, defaultValue = NULL) String name) {
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

    @ShellMethod(value = "Get book by ID", key = {"get book", "gb"})
    public String getBook(@ShellOption(value = {"-I", "--id"}) Long id) {
        var response = bookService.getBook(id);
        if (response.getStatus() == ERROR) {
            return shellHelper.getErrorMessage(response.getMessage());
        }
        return printSuccess(response.getData(), String.format(BOOKS_FOUND, 1));
    }

    @ShellMethod(value = "Add book", key = {"add book", "ab"})
    public String createBook(@ShellOption(value = {"-T", "--title"})
                             @Size(min = MIN_TITLE_LEN, max = MAX_TITLE_LENGTH) String name,
                             @ShellOption(value = {"-A", "--author-id"}) Long authorId,
                             @ShellOption(value = {"-G", "--genre-id"}) Long genreId) {
        var response = bookService.createBook(name, authorId, genreId);
        if (response.getStatus() == ERROR) {
            return shellHelper.getErrorMessage(response.getMessage());
        }
        return printSuccess(response.getData(), BOOK_ADDED);
    }

    @ShellMethod(value = "Modify book", key = {"modify book", "mb"})
    public String modifyBook(@ShellOption(value = {"-I", "--id"}) Long id,
                             @ShellOption(value = {"-T", "--title"}, defaultValue = NULL)
                             @Size(min = MIN_TITLE_LEN, max = MAX_TITLE_LENGTH) String name,
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

    @ShellMethod(value = "Delete book by ID", key = {"delete book", "db"})
    public String deleteBook(@ShellOption(value = {"-I", "--id"}) Long id) {
        var response = bookService.deleteBook(id);
        if (response.getStatus() == ERROR) {
            return shellHelper.getErrorMessage(response.getMessage());
        }
        return printSuccess(response.getData(), BOOK_DELETED);
    }

    @ShellMethod(value = "Delete books by name or author or genre",
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

    @ShellMethod(value = "List book comments", key = {"list comments", "lc"},
            group = "Book comments")
    public String listComments(@ShellOption(value = {"-B", "--book-id"}) Long id) {
        val response = bookService.getBook(id);
        if (response.getStatus() == ERROR) {
            return shellHelper.getErrorMessage(response.getMessage());
        }
        return formatCommentList(response.getData());
    }

    @ShellMethod(value = "Delete comment by ID", key = {"delete comment", "dc"},
            group = "Book comments")
    public String deleteComment(@ShellOption(value = {"-I", "--id"}) Long id) {
        var response = bookService.deleteComment(id);
        if (response.getStatus() == ERROR) {
            return shellHelper.getErrorMessage(response.getMessage());
        }
        return printSuccess(response.getData(), COMMENT_DELETED);
    }

    @ShellMethod(value = "Add comment", key = {"add comment", "ac"},
            group = "Book comments")
    public String createComment(@ShellOption(value = {"-B", "--book-id"}) Long bookId,
            @ShellOption(value = {"-T", "--text"})
            @Size(min = 1, max = MAX_TEXT_LENGTH) String text) {
        var response = bookService.addComment(bookId, new CommentDto(text));
        if (response.getStatus() == ERROR) {
            return shellHelper.getErrorMessage(response.getMessage());
        }
        return printSuccess(response.getData(), COMMENT_ADDED);
    }

    @ShellMethod(value = "Modify comment", key = {"modify comment", "mc"},
            group = "Book comments")
    public String modifyComment(@ShellOption(value = {"-I", "--id"}) Long id,
                                @ShellOption(value = {"-T", "--text"})
                                @Size(min = 1, max = MAX_TEXT_LENGTH) String text) {
        var response = bookService.modifyComment(new CommentDto(id, text));
        if (response.getStatus() == ERROR) {
            return shellHelper.getErrorMessage(response.getMessage());
        }
        return printSuccess(response.getData(), COMMENT_UPDATED);
    }

    private String formatCommentList(BookDto book) {
        val comments = book.getComments();
        val report = String.format(COMMENTS_FOUND, comments.size());
        val sb = new TextStringBuilder().append(format(book)).appendNewLine();
        if (!comments.isEmpty()) {
            val table = createCommentTable(comments);
            sb.append(table);
        }
        return sb.append(shellHelper.getSuccessMessage(report)).toString();
    }

    private String createCommentTable(List<CommentDto> comments) {
        var comparator = comparingLong(CommentDto::getId);
        Function<CommentDto, Object[]> mapper = b -> new Object[]{b.getId(), b.getText()};
        return shellHelper.createTable(comments, comparator, mapper, "ID", "Comments");
    }

    private String format(CommentDto comment) {
        return shellHelper.getInfoMessage(String.format("Comment #%d%n \"%s\"",
                comment.getId(), comment.getText()));
    }

    private String printSuccess(CommentDto comment, String report) {
        return new TextStringBuilder().append(format(comment)).appendNewLine()
                .append(shellHelper.getSuccessMessage(report)).toString();
    }

    private String format(BookDto book) {
        return shellHelper.getInfoMessage(String.format("%s \"%s\", %s, id=%d",
                book.getAuthor().getName(), book.getTitle(), book.getGenre().getName(), book.getId()));
    }

    private String printSuccess(BookDto book, String report) {
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
