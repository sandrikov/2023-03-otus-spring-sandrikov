package ru.otus.homework.quiz.services;

import ru.otus.homework.quiz.model.Answer;
import ru.otus.homework.quiz.model.Question;
import ru.otus.homework.quiz.model.Quiz;
import ru.otus.homework.quiz.model.QuizResult;
import ru.otus.homework.quiz.model.Student;

public interface QuizController {
    void start(Quiz quiz);

    Student getStudent();

    Answer getAnswer(Question question);

    void showResult(QuizResult result);
}
