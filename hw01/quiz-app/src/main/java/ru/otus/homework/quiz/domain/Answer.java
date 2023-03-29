package ru.otus.homework.quiz.domain;

import java.util.LinkedHashSet;
import java.util.Set;

public class Answer {

    private final Question question;

    private final Set<String> answerSet;

    public Answer(Question question) {
        this.question = question;
        this.answerSet = new LinkedHashSet<>();
    }

    public Question getQuestion() {
        return question;
    }

    public Set<String> getAnswerSet() {
        return answerSet;
    }

    public boolean isCorrect() {
        return question.getAnswerSet().size() == answerSet.size() &&
                question.getAnswerSet().containsAll(answerSet);
    }
}
