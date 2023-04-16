package ru.otus.homework.quiz.model;

import java.util.Collection;

public record Quiz(String name, int passingScore, Collection<Question> questions) {

}
