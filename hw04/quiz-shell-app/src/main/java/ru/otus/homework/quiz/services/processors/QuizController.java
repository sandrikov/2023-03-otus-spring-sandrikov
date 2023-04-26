package ru.otus.homework.quiz.services.processors;

import ru.otus.homework.quiz.model.Answer;
import ru.otus.homework.quiz.model.Question;
import ru.otus.homework.quiz.model.Quiz;
import ru.otus.homework.quiz.model.QuizResult;

public interface QuizController {
    void showWelcome(Quiz quiz);

    String[] requestStudentNames();

    Answer requestAnswer(Question question);

    void showResult(QuizResult result);
}
