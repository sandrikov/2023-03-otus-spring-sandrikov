package ru.otus.homework.quiz.model;

import java.util.Collection;

public record Quiz(String name, Collection<Question> questions, int passingScore) {

}
