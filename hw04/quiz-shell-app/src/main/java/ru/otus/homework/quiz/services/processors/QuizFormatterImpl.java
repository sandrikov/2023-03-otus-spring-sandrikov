package ru.otus.homework.quiz.services.processors;

import org.apache.commons.text.TextStringBuilder;
import org.springframework.stereotype.Service;
import ru.otus.homework.quiz.model.Question;
import ru.otus.homework.quiz.model.Quiz;
import ru.otus.homework.quiz.model.QuizResult;
import ru.otus.homework.quiz.services.localization.Localizer;

import java.util.Formatter;

import static ru.otus.homework.quiz.services.processors.ConsoleColor.BLUE_BOLD;
import static ru.otus.homework.quiz.services.processors.ConsoleColor.CYAN;
import static ru.otus.homework.quiz.services.processors.ConsoleColor.GREEN_BOLD_BRIGHT;
import static ru.otus.homework.quiz.services.processors.ConsoleColor.RED_BOLD_BRIGHT;
import static ru.otus.homework.quiz.services.processors.ConsoleColor.RED_BRIGHT;
import static ru.otus.homework.quiz.services.processors.ConsoleColor.RESET;

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

        sb.appendNewLine().append(BLUE_BOLD);
        sb.append(split).appendNewLine();
        sb.append(lineQuiz).appendNewLine();
        sb.append(split).append(RESET).appendNewLine();

        return sb.toString();
    }

    @Override
    public String getQuestionPrompt(Question question) {
        try (var formatter = new Formatter()) {
            formatter.format("%s%-2s%s %-35s:",
                    BLUE_BOLD, question.id(), RESET, question.text());
            question.availableOptions()
                    .forEach(o -> formatter.format("%n   %s%-2s%s %-35s", CYAN, o.id(), RESET, o.text()));
            return formatter.toString();
        }
    }

    @Override
    public String getResultMessage(QuizResult result) {
        var sb = new TextStringBuilder();
        var maxPossibleScore = result.quiz().questions().size();

        var scoreMsg = localizer.getMessage("quiz.result", result.score(), maxPossibleScore);
        sb.appendNewLine().append(BLUE_BOLD);
        sb.append(result.student().surname()).append(" ").append(result.student().firstName()).append(", ");
        sb.append(scoreMsg);
        if (result.passed()) {
            var resMsg = localizer.getMessage("quiz.passed");
            sb.appendNewLine().append(GREEN_BOLD_BRIGHT);
            sb.append(resMsg);
        } else {
            var passScoreMsg = localizer.getMessage("quiz.passing-score", result.quiz().passingScore());
            var resMsg = localizer.getMessage("quiz.failed");
            sb.appendNewLine().append(RED_BRIGHT);
            sb.append(passScoreMsg);
            sb.appendNewLine().append(RED_BOLD_BRIGHT);
            sb.append(resMsg);
        }

        return sb.append(RESET).toString();
    }

    @Override
    public String getStudentPrompt() {
        return localizer.getMessage("quiz.student-prompt");
    }


}
