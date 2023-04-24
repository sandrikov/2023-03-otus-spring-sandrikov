package ru.otus.homework.quiz.services.localization;

import jakarta.validation.constraints.NotNull;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Locale;

@Service
public class LocaleServiceImpl implements LocaleService {
    private final AppLocale appLocale;

    private final MessageSource messageSource;

    public LocaleServiceImpl(AppLocale appLocale, MessageSource messageSource) {
        this.appLocale = appLocale;
        this.messageSource = messageSource;
    }

    @Override
    public void changeLocale(@NotNull String localeValue) throws WrongLocale {
        var defaultLocale = Locale.getDefault();
        try {
            var locale = StringUtils.parseLocale(localeValue);
            if (locale != null) {
                // set unknown default locale for MessageSource
                Locale.setDefault(Locale.ROOT);
                // test using the new locale
                messageSource.getMessage("shell.locale.wrong", null, locale);
                appLocale.setLocale(locale);
            }
        } catch (Exception ex) {
            var message = messageSource.getMessage("shell.locale.wrong", new Object[]{localeValue},
                    appLocale.getLocale());
            throw new WrongLocale(message, ex);
        } finally {
            // reset default locale
            Locale.setDefault(defaultLocale);
        }
    }

    @Override
    public String getCurrentLocaleMessage() {
        var locale = appLocale.getLocale();
        return messageSource.getMessage("shell.locale.current", new Object[]{locale}, locale);
    }
}
