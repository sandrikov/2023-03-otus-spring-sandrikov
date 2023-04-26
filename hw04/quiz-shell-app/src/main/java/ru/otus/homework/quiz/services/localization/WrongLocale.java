package ru.otus.homework.quiz.services.localization;

import java.io.Serial;

public class WrongLocale extends Exception {
    @Serial
    private static final long serialVersionUID = 6223215362457371642L;

    public WrongLocale(String message, Exception ex) {
        super(message, ex);
    }
}
