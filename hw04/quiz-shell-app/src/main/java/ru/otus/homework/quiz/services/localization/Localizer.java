package ru.otus.homework.quiz.services.localization;

import org.springframework.lang.Nullable;

public interface Localizer {

    @SuppressWarnings("unused")
    String localize(String source);

    String getMessage(String code, @Nullable Object... args);
}
