package ru.otus.homework.quiz.services;

import org.apache.commons.text.TextStringBuilder;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ru.otus.homework.quiz.model.Question;
import ru.otus.homework.quiz.model.Quiz;
import ru.otus.homework.quiz.model.QuizResult;
import ru.otus.homework.quiz.services.localize.Localizer;

import java.util.Formatter;

@Service
public class QuizFormatterImpl implements QuizFormatter {

    private final Localizer localizer;

    public QuizFormatterImpl(Localizer localizer) {
        this.localizer = localizer;
    }

    @Override
    public String getHeader(Quiz quiz) {
        var sb = new TextStringBuilder();

        var lineQuiz = "*     " + localizer.getMessage("quiz.welcome", quiz.name()) + "     *";
        var split = "*".repeat(lineQuiz.length());

        sb.appendNewLine().append(Color.BLUE_BOLD);
        sb.append(split).appendNewLine();
        sb.append(lineQuiz).appendNewLine();
        sb.append(split).append(Color.RESET).appendNewLine();

        return sb.toString();
    }

    @Override
    public String getQuestionPrompt(Question question) {
        try (Formatter formatter = new Formatter()) {
            formatter.format("%s%-2s%s %-35s:", Color.BLUE_BOLD, question.id(), Color.RESET, question.text());
            question.availableOptions()
                    .forEach(o -> formatter.format("%n   %s%-2s%s %-35s", Color.CYAN, o.id(), Color.RESET, o.text()));
            return formatter.toString();
        }
    }

    @Override
    public String getResultMessage(QuizResult result) {
        var sb = new TextStringBuilder();
        var maxPossibleScore = result.quiz().questions().size();

        var scoreMsg = localizer.getMessage("quiz.result", result.score(), maxPossibleScore);
        sb.appendNewLine().append(Color.BLUE_BOLD);
        sb.append(result.student().surname()).append(" ").append(result.student().firstName()).append(", ");
        sb.append(scoreMsg);
        if (result.passed()) {
            var resMsg = localizer.getMessage("quiz.passed");
            sb.appendNewLine().append(Color.GREEN_BOLD_BRIGHT);
            sb.append(resMsg);
        } else {
            var passScoreMsg = localizer.getMessage("quiz.passing-score", result.quiz().passingScore());
            var resMsg = localizer.getMessage("quiz.failed");
            sb.appendNewLine().append(Color.RED_BRIGHT);
            sb.append(passScoreMsg);
            sb.appendNewLine().append(Color.RED_BOLD_BRIGHT);
            sb.append(resMsg);
        }

        return sb.append(Color.RESET).toString();
    }

    @Override
    public String getStudentPrompt() {
        return localizer.getMessage("quiz.student-prompt");
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
        sb.append(Color.RED_BRIGHT).append(message);
        if (info != null) {
            sb.append(": ").append(Color.GREEN_BOLD_BRIGHT).append(info);
        }
        return sb.append(Color.RESET).toString();
    }

    enum Color {
        //Color end string, color reset
        RESET("\033[0m"),

        // Regular Colors. Normal color, no bold, background color etc.
        BLACK("\033[0;30m"),    // BLACK
        RED("\033[0;31m"),      // RED
        GREEN("\033[0;32m"),    // GREEN
        YELLOW("\033[0;33m"),   // YELLOW
        BLUE("\033[0;34m"),     // BLUE
        MAGENTA("\033[0;35m"),  // MAGENTA
        CYAN("\033[0;36m"),     // CYAN
        WHITE("\033[0;37m"),    // WHITE

        // Bold
        BLACK_BOLD("\033[1;30m"),   // BLACK
        RED_BOLD("\033[1;31m"),     // RED
        GREEN_BOLD("\033[1;32m"),   // GREEN
        YELLOW_BOLD("\033[1;33m"),  // YELLOW
        BLUE_BOLD("\033[1;34m"),    // BLUE
        MAGENTA_BOLD("\033[1;35m"), // MAGENTA
        CYAN_BOLD("\033[1;36m"),    // CYAN
        WHITE_BOLD("\033[1;37m"),   // WHITE

        // Underline
        BLACK_UNDERLINED("\033[4;30m"),     // BLACK
        RED_UNDERLINED("\033[4;31m"),       // RED
        GREEN_UNDERLINED("\033[4;32m"),     // GREEN
        YELLOW_UNDERLINED("\033[4;33m"),    // YELLOW
        BLUE_UNDERLINED("\033[4;34m"),      // BLUE
        MAGENTA_UNDERLINED("\033[4;35m"),   // MAGENTA
        CYAN_UNDERLINED("\033[4;36m"),      // CYAN
        WHITE_UNDERLINED("\033[4;37m"),     // WHITE

        // Background
        BLACK_BACKGROUND("\033[40m"),   // BLACK
        RED_BACKGROUND("\033[41m"),     // RED
        GREEN_BACKGROUND("\033[42m"),   // GREEN
        YELLOW_BACKGROUND("\033[43m"),  // YELLOW
        BLUE_BACKGROUND("\033[44m"),    // BLUE
        MAGENTA_BACKGROUND("\033[45m"), // MAGENTA
        CYAN_BACKGROUND("\033[46m"),    // CYAN
        WHITE_BACKGROUND("\033[47m"),   // WHITE

        // High Intensity
        BLACK_BRIGHT("\033[0;90m"),     // BLACK
        RED_BRIGHT("\033[0;91m"),       // RED
        GREEN_BRIGHT("\033[0;92m"),     // GREEN
        YELLOW_BRIGHT("\033[0;93m"),    // YELLOW
        BLUE_BRIGHT("\033[0;94m"),      // BLUE
        MAGENTA_BRIGHT("\033[0;95m"),   // MAGENTA
        CYAN_BRIGHT("\033[0;96m"),      // CYAN
        WHITE_BRIGHT("\033[0;97m"),     // WHITE

        // Bold High Intensity
        BLACK_BOLD_BRIGHT("\033[1;90m"),    // BLACK
        RED_BOLD_BRIGHT("\033[1;91m"),      // RED
        GREEN_BOLD_BRIGHT("\033[1;92m"),    // GREEN
        YELLOW_BOLD_BRIGHT("\033[1;93m"),   // YELLOW
        BLUE_BOLD_BRIGHT("\033[1;94m"),     // BLUE
        MAGENTA_BOLD_BRIGHT("\033[1;95m"),  // MAGENTA
        CYAN_BOLD_BRIGHT("\033[1;96m"),     // CYAN
        WHITE_BOLD_BRIGHT("\033[1;97m"),    // WHITE

        // High Intensity backgrounds
        BLACK_BACKGROUND_BRIGHT("\033[0;100m"),     // BLACK
        RED_BACKGROUND_BRIGHT("\033[0;101m"),       // RED
        GREEN_BACKGROUND_BRIGHT("\033[0;102m"),     // GREEN
        YELLOW_BACKGROUND_BRIGHT("\033[0;103m"),    // YELLOW
        BLUE_BACKGROUND_BRIGHT("\033[0;104m"),      // BLUE
        MAGENTA_BACKGROUND_BRIGHT("\033[0;105m"),   // MAGENTA
        CYAN_BACKGROUND_BRIGHT("\033[0;106m"),      // CYAN
        WHITE_BACKGROUND_BRIGHT("\033[0;107m");     // WHITE

        private final String code;

        Color(String code) {
            this.code = code;
        }

        @Override
        public String toString() {
            return code;
        }
    }
}
