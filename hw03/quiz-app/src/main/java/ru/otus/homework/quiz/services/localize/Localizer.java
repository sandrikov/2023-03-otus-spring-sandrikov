package ru.otus.homework.quiz.services.localize;

import org.springframework.lang.Nullable;

public interface Localizer {
    String localize(String source);

    String getMessage(String code, @Nullable Object... args);
}
