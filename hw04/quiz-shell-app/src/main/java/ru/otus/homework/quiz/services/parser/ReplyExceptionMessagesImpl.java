package ru.otus.homework.quiz.services.parser;

import org.apache.commons.text.TextStringBuilder;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ru.otus.homework.quiz.services.localization.Localizer;

import static ru.otus.homework.quiz.services.processors.ConsoleColor.GREEN_BOLD_BRIGHT;
import static ru.otus.homework.quiz.services.processors.ConsoleColor.RED_BRIGHT;
import static ru.otus.homework.quiz.services.processors.ConsoleColor.RESET;

@Service
public class ReplyExceptionMessagesImpl implements ReplyExceptionMessages {

    private final Localizer localizer;

    public ReplyExceptionMessagesImpl(Localizer localizer) {
        this.localizer = localizer;
    }

    @Override
    public String getExpectedOneOptionMessage() {
        return getErrorMessage("error.only-one-option", null);
    }

    @Override
    public String getInvalidOptionMessage(String optionId) {
        return getErrorMessage("error.wrong-option", optionId);
    }

    @Override
    public String getMustBeUniqueMessage(String optionId) {
        return getErrorMessage("error.non-unique-option", optionId);
    }

    private String getErrorMessage(String code, @Nullable String info) {
        var message = localizer.getMessage(code);
        var sb = new TextStringBuilder();
        sb.append(RED_BRIGHT).append(message);
        if (info != null) {
            sb.append(": ").append(GREEN_BOLD_BRIGHT).append(info);
        }
        return sb.append(RESET).toString();
    }

}
