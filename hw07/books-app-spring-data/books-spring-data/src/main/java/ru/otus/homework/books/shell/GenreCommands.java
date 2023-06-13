package ru.otus.homework.books.shell;

import jakarta.validation.constraints.Size;
import org.apache.commons.text.TextStringBuilder;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.homework.books.dto.GenreDto;
import ru.otus.homework.books.services.GenreService;

import static java.util.Comparator.comparing;
import static ru.otus.homework.books.domain.SchemaSqlConstants.MAX_NAME_LENGTH;
import static ru.otus.homework.books.services.ServiceResponse.Status.ERROR;

@ShellComponent
@ShellCommandGroup("Book Genres")
public class GenreCommands {

    public static final String GENRES_FOUND = "%d genres found.";

    public static final String GENRE_ADDED = "Add genre is done.";

    public static final String GENRE_RENAMED = "The genre is renamed.";

    public static final String GENRE_DELETED = "Delete genre is done.";

    public static final String[] TABLE_HEADER_ROW = {"Name", "ID"};

    private static final int MIN_NAME_LENGTH = 2;

    private final ShellHelper shellHelper;

    private final GenreService genreService;

    public GenreCommands(ShellHelper shellHelper, GenreService genreService) {
        this.shellHelper = shellHelper;
        this.genreService = genreService;
    }

    @ShellMethod(value = "Lists genres", key = {"list genres", "lg"})
    public String listGenres() {
        var genres = genreService.listGenres().getData();
        var report = String.format(GENRES_FOUND, genres.size());
        if (genres.isEmpty()) {
            return shellHelper.getWarningMessage(report);
        }
        var table = shellHelper.createTable(genres, comparing(GenreDto::getName),
                a -> new Object[]{a.getName(), a.getId()}, TABLE_HEADER_ROW);
        return table.concat(shellHelper.getSuccessMessage(report));
    }

    @ShellMethod(value = "Get genre by ID", key = {"get genre", "gg"})
    public String getGenre(@ShellOption(value = {"-I", "--id"}) Long id) {
        var response = genreService.getGenre(id);
        if (response.getStatus() == ERROR) {
            return shellHelper.getErrorMessage(response.getMessage());
        }
        return printSuccess(response.getData(), String.format(GENRES_FOUND, 1));
    }

    @ShellMethod(value = "Find genre by name", key = {"find genre", "fg"})
    public String findGenre(@ShellOption(value = {"-N", "--name"})
                            @Size(min = MIN_NAME_LENGTH, max = MAX_NAME_LENGTH) String name) {
        var response = genreService.findGenre(name);
        if (response.getStatus() == ERROR) {
            return shellHelper.getErrorMessage(response.getMessage());
        }
        return printSuccess(response.getData(), String.format(GENRES_FOUND, 1));
    }

    @ShellMethod(value = "Add genre", key = {"add genre", "ag"})
    public String createGenre(@ShellOption(value = {"-N", "--name"})
                              @Size(min = MIN_NAME_LENGTH, max = MAX_NAME_LENGTH) String name) {
        var response = genreService.createGenre(name);
        if (response.getStatus() == ERROR) {
            return shellHelper.getErrorMessage(response.getMessage());
        }
        return printSuccess(response.getData(), GENRE_ADDED);
    }

    @ShellMethod(value = "Rename genre", key = {"rename genre", "rg"})
    public String renameGenre(@ShellOption(value = {"-I", "--id"}) Long id,
                              @ShellOption(value = {"-N", "--name"})
                              @Size(min = MIN_NAME_LENGTH, max = MAX_NAME_LENGTH) String name) {
        var response = genreService.renameGenre(id, name);
        if (response.getStatus() == ERROR) {
            return shellHelper.getErrorMessage(response.getMessage());
        }
        return printSuccess(response.getData(), GENRE_RENAMED);
    }

    @ShellMethod(value = "Delete genre", key = {"delete genre", "dg"})
    public String deleteGenre(@ShellOption(value = {"-I", "--id"}) Long id) {
        var response = genreService.deleteGenre(id);
        if (response.getStatus() == ERROR) {
            return shellHelper.getErrorMessage(response.getMessage());
        }
        return printSuccess(response.getData(), GENRE_DELETED);
    }

    private String format(GenreDto genre) {
        return shellHelper.getInfoMessage(String.format("%s id=%d", genre.getName(), genre.getId()));
    }

    private String printSuccess(GenreDto genre, String report) {
        return new TextStringBuilder().append(format(genre)).appendNewLine()
                .append(shellHelper.getSuccessMessage(report)).toString();
    }
}
