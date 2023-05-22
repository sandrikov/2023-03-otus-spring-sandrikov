package ru.otus.homework.books.shell;

import jakarta.validation.constraints.Size;
import org.apache.commons.text.TextStringBuilder;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.homework.books.model.Author;
import ru.otus.homework.books.services.AuthorService;

import static java.util.Comparator.comparing;
import static ru.otus.homework.books.services.ServiceResponse.Status.ERROR;

@ShellComponent
public class AuthorCommands {

    public static final String AUTHORS_FOUND = "%d authors found.";

    public static final String AUTHOR_ADDED = "Add author is done.";

    public static final String AUTHOR_RENAMED = "The author is renamed.";

    public static final String AUTHOR_DELETED = "Delete author is done.";

    public static final String[] TABLE_HEADER_ROW = {"Name", "ID"};

    private static final String COMMAND_GROUP_AUTHOR = "Book Authors";

    private static final int MIN_NAME_LEN = 2;

    private static final int MAX_NAME_LEN = 255;

    private final ShellHelper shellHelper;

    private final AuthorService authorService;

    public AuthorCommands(ShellHelper shellHelper, AuthorService authorService) {
        this.shellHelper = shellHelper;
        this.authorService = authorService;
    }

    @ShellMethod(value = "Lists authors", key = {"list-authors", "la"}, group = COMMAND_GROUP_AUTHOR)
    public String listAuthors() {
        var authors = authorService.listAuthors().getData();
        var report = String.format(AUTHORS_FOUND, authors.size());
        if (authors.isEmpty()) {
            return shellHelper.getWarningMessage(report);
        }
        var table = shellHelper.createTable(authors, comparing(Author::getName),
                a -> new Object[]{a.getName(), a.getId()}, TABLE_HEADER_ROW);
        return table.concat(shellHelper.getSuccessMessage(report));
    }

    @ShellMethod(value = "Get author by ID", key = {"get-author", "ga"}, group = COMMAND_GROUP_AUTHOR)
    public String getAuthor(@ShellOption(value = {"-I", "--id"}) Long id) {
        var response = authorService.getAuthor(id);
        if (response.getStatus() == ERROR) {
            return shellHelper.getErrorMessage(response.getMessage());
        }
        return printSuccess(response.getData(), String.format(AUTHORS_FOUND, 1));
    }

    @ShellMethod(value = "Find author by name", key = {"find-author", "fa"}, group = COMMAND_GROUP_AUTHOR)
    public String findAuthor(@ShellOption(value = {"-N", "--name"})
                             @Size(min = MIN_NAME_LEN, max = MAX_NAME_LEN) String name) {
        var response = authorService.findAuthor(name);
        if (response.getStatus() == ERROR) {
            return shellHelper.getErrorMessage(response.getMessage());
        }
        return printSuccess(response.getData(), String.format(AUTHORS_FOUND, 1));
    }

    @ShellMethod(value = "Add author", key = {"add-author", "aa"}, group = COMMAND_GROUP_AUTHOR)
    public String createAuthor(@ShellOption(value = {"-N", "--name"})
                               @Size(min = MIN_NAME_LEN, max = MAX_NAME_LEN) String name) {
        var response = authorService.createAuthor(name);
        if (response.getStatus() == ERROR) {
            return shellHelper.getErrorMessage(response.getMessage());
        }
        return printSuccess(response.getData(), AUTHOR_ADDED);
    }

    @ShellMethod(value = "Rename author", key = {"rename-author", "ra"}, group = COMMAND_GROUP_AUTHOR)
    public String renameAuthor(@ShellOption(value = {"-I", "--id"}) Long id,
                               @ShellOption(value = {"-N", "--name"})
                               @Size(min = MIN_NAME_LEN, max = MAX_NAME_LEN) String name) {
        var response = authorService.renameAuthor(id, name);
        if (response.getStatus() == ERROR) {
            return shellHelper.getErrorMessage(response.getMessage());
        }
        return printSuccess(response.getData(), AUTHOR_RENAMED);
    }

    @ShellMethod(value = "Delete author", key = {"delete-author", "da"}, group = COMMAND_GROUP_AUTHOR)
    public String deleteAuthor(@ShellOption(value = {"-I", "--id"}) Long id) {
        var response = authorService.deleteAuthor(id);
        if (response.getStatus() == ERROR) {
            return shellHelper.getErrorMessage(response.getMessage());
        }
        return printSuccess(response.getData(), AUTHOR_DELETED);
    }


    private String format(Author author) {
        return shellHelper.getInfoMessage(String.format("%s id=%d", author.getName(), author.getId()));
    }

    private String printSuccess(Author author, String report) {
        return new TextStringBuilder().append(format(author)).appendNewLine()
                .append(shellHelper.getSuccessMessage(report)).toString();
    }
}
