package ru.otus.homework.books.shell;


import lombok.Setter;
import org.jline.utils.AttributedStyle;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Setter
@ConfigurationProperties(prefix = "shell.out")
public class ShellColorProps implements ShellColors {

    private PromptColor info;

    private PromptColor success;

    private PromptColor warning;

    private PromptColor error;

    private PromptColor command;

    @Override
    public int getInfoColor() {
        return nvl(info);
    }

    @Override
    public int getSuccessColor() {
        return nvl(success);
    }

    @Override
    public int getWarningColor() {
        return nvl(warning);
    }

    @Override
    public int getErrorColor() {
        return nvl(error);
    }

    @Override
    public int getCommandColor() {
        return nvl(command);
    }

    private int nvl(PromptColor promptColor) {
        return promptColor != null ? promptColor.toJlineAttributedStyle() : AttributedStyle.BLACK;
    }

    private enum PromptColor {
        BLACK(AttributedStyle.BLACK),
        RED(AttributedStyle.RED),
        GREEN(AttributedStyle.GREEN),
        YELLOW(AttributedStyle.YELLOW),
        BLUE(AttributedStyle.BLUE),
        MAGENTA(AttributedStyle.MAGENTA),
        CYAN(AttributedStyle.CYAN),
        WHITE(AttributedStyle.WHITE),
        BRIGHT(AttributedStyle.BRIGHT),

        NONE(-1);

        private final int value;

        PromptColor(int value) {
            this.value = value;
        }

        public int toJlineAttributedStyle() {
            return this.value;
        }
    }
}
