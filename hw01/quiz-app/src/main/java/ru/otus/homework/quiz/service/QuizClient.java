package ru.otus.homework.quiz.service;

import ru.otus.homework.quiz.domain.Quiz;
import ru.otus.homework.quiz.domain.QuizResult;

public interface QuizClient {

    QuizResult doTest(Quiz task);

}
