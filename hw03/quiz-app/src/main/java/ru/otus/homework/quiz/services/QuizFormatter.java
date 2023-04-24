package ru.otus.homework.quiz.services;

import ru.otus.homework.quiz.model.Question;
import ru.otus.homework.quiz.model.Quiz;
import ru.otus.homework.quiz.model.QuizResult;

interface QuizFormatter {

    String getHeader(Quiz quiz);

    String getResultMessage(QuizResult result);

    String getStudentPrompt();

    String getExpectedOneOptionMessage();

    String getInvalidOptionMessage(String optionId);

    String getMustBeUniqueMessage(String optionId);

    String getQuestionPrompt(Question question);

}
