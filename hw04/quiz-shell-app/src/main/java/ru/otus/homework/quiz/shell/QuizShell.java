package ru.otus.homework.quiz.shell;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.homework.quiz.services.QuizService;
import ru.otus.homework.quiz.services.localization.LocaleService;
import ru.otus.homework.quiz.services.localization.WrongLocale;
import ru.otus.homework.quiz.services.report.QuizReporting;

import static org.springframework.shell.standard.ShellOption.NULL;


@ShellComponent
public class QuizShell {

    private final QuizService quizService;

    private final QuizReporting quizReporting;

    private final LocaleService localeService;

    public QuizShell(LocaleService localeService, QuizService quizService, QuizReporting quizReporting) {
        this.quizService = quizService;
        this.quizReporting = quizReporting;
        this.localeService = localeService;
    }

    @ShellMethod(value = "Start quiz.", key = {"s", "start"})
    public void start() {
        quizService.start();
    }

    @ShellMethod(value = "Print statistics.", key = {"r", "report"})
    public String report() {
        return quizReporting.getStatistic();
    }

    @ShellMethod(value = "Change locale.", key = {"l", "locale"})
    public String locale(@ShellOption(help = "Valid locale code", defaultValue = NULL) String locale) {
        if (locale != null) {
            try {
                localeService.changeLocale(locale);
            } catch (WrongLocale e) {
                return e.getMessage();
            }
        }
        return localeService.getCurrentLocaleMessage();
    }

}
