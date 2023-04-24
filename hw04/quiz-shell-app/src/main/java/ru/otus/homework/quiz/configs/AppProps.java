package ru.otus.homework.quiz.configs;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import ru.otus.homework.quiz.services.localization.AppLocale;

import java.util.Locale;


@Component
@ConfigurationProperties(prefix = "application")
public class AppProps implements AppLocale {

    private String version;

    private Locale locale;

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

}
