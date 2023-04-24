package ru.otus.homework.quiz.services.parser;

@FunctionalInterface
public interface ReplyParser<T> {

    T parse(String input) throws ReplyException;

}
