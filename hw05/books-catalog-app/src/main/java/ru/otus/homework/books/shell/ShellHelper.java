package ru.otus.homework.books.shell;

import lombok.Setter;
import org.jline.terminal.Terminal;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;
import org.springframework.shell.table.ArrayTableModel;
import org.springframework.shell.table.BorderStyle;
import org.springframework.shell.table.CellMatchers;
import org.springframework.shell.table.SimpleHorizontalAligner;
import org.springframework.shell.table.TableBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

import static org.jline.utils.AttributedStyle.DEFAULT;

@Setter
public class ShellHelper {

    private final Terminal terminal;

    private final ShellColors shellColors;

    public ShellHelper(Terminal terminal, ShellColors shellColors) {
        this.terminal = terminal;
        this.shellColors = shellColors;
    }

    public String getColored(String message, int foregroundColor) {
        return getColored(message, foregroundColor == -1 ? null : DEFAULT.foreground(foregroundColor));
    }

    public String getColored(String message) {
        return getColored(message, null);
    }

    public String getColored(String message, AttributedStyle attributedStyle) {
        return (new AttributedStringBuilder()).append(message, attributedStyle).toAnsi(terminal);
    }

    public String getInfoMessage(String message) {
        return getColored(message, shellColors.getInfoColor());
    }

    public String getSuccessMessage(String message) {
        return getColored(message, shellColors.getSuccessColor());
    }

    public String getWarningMessage(String message) {
        return getColored(message, shellColors.getWarningColor());
    }

    public String getErrorMessage(String message) {
        return getColored(message, shellColors.getErrorColor());
    }

    public String getCommandMessage(String message) {
        return getColored(message, shellColors.getCommandColor());
    }

    public void printSuccess(String message) {
        print(message, shellColors.getSuccessColor());
    }

    public void printInfo(String message) {
        print(message, shellColors.getInfoColor());
    }

    public void printWarning(String message) {
        print(message, shellColors.getWarningColor());
    }

    public void printError(String message) {
        print(message, shellColors.getErrorColor());
    }

    public void printCommand(String message) {
        print(message, shellColors.getCommandColor());
    }

    public void print(String message) {
        var toPrint = getColored(message);
        terminal.writer().println(toPrint);
        terminal.flush();
    }

    public void print(String message, int color) {
        var toPrint = getColored(message, color);
        terminal.writer().println(toPrint);
        terminal.flush();
    }

    public <T> void printTable(Collection<T> list, Comparator<T> comparator,
                               Function<T, Object[]> mapper, String... headerRow) {
        print(createTable(list, comparator, mapper, headerRow));
    }

    public <T> String createTable(Collection<T> list, Comparator<T> comparator,
                               Function<T, Object[]> mapper, String... headerRow) {
        List<Object[]> data = new ArrayList<>();
        data.add(headerRow);
        list.stream().sorted(comparator).map(mapper).forEach(data::add);
        var model = new ArrayTableModel(data.toArray(Object[][]::new));
        var builder = new TableBuilder(model);
        return applyStyle(builder).build().render(120);
    }

    private TableBuilder applyStyle(TableBuilder builder) {
        builder.addHeaderAndVerticalsBorders(BorderStyle.oldschool);
        builder.on(CellMatchers.ofType(Long.class)).addAligner(SimpleHorizontalAligner.right);
        builder.on(CellMatchers.row(0)).addAligner(SimpleHorizontalAligner.center);
        return builder;
    }

}
