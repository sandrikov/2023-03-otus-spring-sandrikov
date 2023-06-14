package ru.otus.homework.books.shell;


import jakarta.validation.constraints.Size;
import lombok.val;
import org.apache.commons.text.TextStringBuilder;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.homework.books.dto.BookDto;
import ru.otus.homework.books.dto.CommentDto;
import ru.otus.homework.books.services.BookService;
import ru.otus.homework.books.services.CommentService;

import java.util.List;
import java.util.function.Function;

import static java.util.Comparator.comparingLong;
import static ru.otus.homework.books.domain.SchemaSqlConstants.MAX_TEXT_LENGTH;
import static ru.otus.homework.books.services.ServiceResponse.Status.ERROR;

@ShellComponent
@ShellCommandGroup("Book comments")
public class CommentCommands {

    public static final String COMMENTS_FOUND = "%d comments found.";

    public static final String COMMENT_DELETED = "Delete comment is done.";

    public static final String COMMENT_ADDED = "Add comment is done.";

    public static final String COMMENT_UPDATED = "Modify comment is done.";

    private final CommentService commentService;

    private final BookService bookService;

    private final ShellHelper shellHelper;

    public CommentCommands(CommentService commentService, BookService bookService, ShellHelper shellHelper) {
        this.commentService = commentService;
        this.bookService = bookService;
        this.shellHelper = shellHelper;
    }

    @ShellMethod(value = "List book comments", key = {"list comments", "lc"})
    public String listComments(@ShellOption(value = {"-B", "--book-id"}) Long id) {
        val response = bookService.getBook(id);
        if (response.getStatus() == ERROR) {
            return shellHelper.getErrorMessage(response.getMessage());
        }
        return formatCommentList(response.getData());
    }

    @ShellMethod(value = "Delete comment by ID", key = {"delete comment", "dc"})
    public String deleteComment(@ShellOption(value = {"-I", "--id"}) Long id) {
        var response = commentService.deleteComment(id);
        if (response.getStatus() == ERROR) {
            return shellHelper.getErrorMessage(response.getMessage());
        }
        return printSuccess(response.getData(), COMMENT_DELETED);
    }

    @ShellMethod(value = "Add comment", key = {"add comment", "ac"})
    public String createComment(@ShellOption(value = {"-B", "--book-id"}) Long bookId,
                                @ShellOption(value = {"-T", "--text"})
                                @Size(min = 1, max = MAX_TEXT_LENGTH) String text) {
        var response = commentService.addComment(bookId, new CommentDto(text));
        if (response.getStatus() == ERROR) {
            return shellHelper.getErrorMessage(response.getMessage());
        }
        return printSuccess(response.getData(), COMMENT_ADDED);
    }

    @ShellMethod(value = "Modify comment", key = {"modify comment", "mc"})
    public String modifyComment(@ShellOption(value = {"-I", "--id"}) Long id,
                                @ShellOption(value = {"-T", "--text"})
                                @Size(min = 1, max = MAX_TEXT_LENGTH) String text) {
        var response = commentService.modifyComment(new CommentDto(id, text));
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

    private String format(BookDto book) {
        return shellHelper.getInfoMessage(String.format("%s \"%s\", %s, id=%d",
                book.getAuthor().getName(), book.getTitle(), book.getGenre().getName(), book.getId()));
    }

    private String printSuccess(CommentDto comment, String report) {
        return new TextStringBuilder().append(format(comment)).appendNewLine()
                .append(shellHelper.getSuccessMessage(report)).toString();
    }

}
