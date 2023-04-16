package ru.otus.homework.quiz.services.localize;

import org.apache.commons.text.StringSubstitutor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ru.otus.homework.quiz.services.AppLocale;

@Service
public class LocalizerImpl implements Localizer {
    private final MessageSource messageSource;

    private final AppLocale appLocale;

    public LocalizerImpl(MessageSource messageSource, AppLocale appLocale) {
        this.messageSource = messageSource;
        this.appLocale = appLocale;
    }

    @Override
    public String localize(String source) {
        var stringSubstitutor = new StringSubstitutor(
                key -> messageSource.getMessage(key, null, appLocale.getLocale()));
        return stringSubstitutor.replace(source);
    }

    @Override
    public String getMessage(String code, Object... args) {
        return messageSource.getMessage(code, args, appLocale.getLocale());
    }
}
