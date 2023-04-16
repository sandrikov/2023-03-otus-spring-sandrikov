package ru.otus.homework.quiz.configs;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import ru.otus.homework.quiz.dao.QuizRepository;
import ru.otus.homework.quiz.model.Quiz;
import ru.otus.homework.quiz.services.AppLocale;

import java.util.Locale;


@Component
@ConfigurationProperties(prefix = "application")
public class AppProps implements AppLocale, QuizRepository {

    private String version;

    private Locale locale;

    private Quiz quiz;

    @Override
    public Locale getLocale() {
        return locale;
    }

    @Override
    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }
}
