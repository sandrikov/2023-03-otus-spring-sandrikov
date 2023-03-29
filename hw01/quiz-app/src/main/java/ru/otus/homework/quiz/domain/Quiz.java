package ru.otus.homework.quiz.domain;

import java.util.Deque;

public record Quiz(String name, Deque<Question> questionDeque) {

}
