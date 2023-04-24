package ru.otus.homework.quiz.boot;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import ru.otus.homework.quiz.services.AppLocale;
import ru.otus.homework.quiz.services.QuizService;

@Component
public class QuizCommandLineRunner implements CommandLineRunner {

    private final QuizService quizService;

    private final AppLocale appLocale;

    public QuizCommandLineRunner(QuizService quizService, AppLocale appLocale) {
        this.quizService = quizService;
        this.appLocale = appLocale;
    }

    @Override
    public void run(String... args) {
        if (args != null) {
            for (var arg : args) {
                var prefix = "locale=";
                if (arg.startsWith(prefix)) {
                    var code = arg.substring(prefix.length());
                    var locale = StringUtils.parseLocale(code);
                    appLocale.setLocale(locale);
                }
            }
        }
        quizService.start();
    }
}
