package ru.otus.homework.quiz.services.localization;

public interface LocaleService {

    void changeLocale(String locale) throws WrongLocale;

    String getCurrentLocaleMessage();
}
