package ru.otus.homework.quiz.services;

@FunctionalInterface
public interface ReplyParser<T> {

    T parse(String t) throws ReplyException;

}
