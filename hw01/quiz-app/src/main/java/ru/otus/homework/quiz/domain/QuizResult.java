package ru.otus.homework.quiz.domain;

import java.util.ArrayList;
import java.util.List;

public class QuizResult {
    private final String student;

    private final List<Answer> answerList;

    public QuizResult(String student) {
        this.student = student;
        this.answerList = new ArrayList<>();
    }

    public List<Answer> getAnswerList() {
        return answerList;
    }

    public String getStudent() {
        return student;
    }

    public int score() {
        return (int) answerList.stream().filter(Answer::isCorrect).count();
    }
}
