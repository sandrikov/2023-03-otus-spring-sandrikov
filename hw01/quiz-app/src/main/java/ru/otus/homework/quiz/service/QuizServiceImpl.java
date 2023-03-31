package ru.otus.homework.quiz.service;

import ru.otus.homework.quiz.dao.QuizDao;
import ru.otus.homework.quiz.domain.Quiz;

public class QuizServiceImpl implements QuizService {

    private final QuizDao dao;

    public QuizServiceImpl(QuizDao dao) {
        this.dao = dao;
    }

    public Quiz getQuiz() {
        return dao.loadQuiz();
    }

}
