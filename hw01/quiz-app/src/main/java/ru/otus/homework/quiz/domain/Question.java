package ru.otus.homework.quiz.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Question {

    private final String id;

    private final String text;

    private final Set<String> answerSet;

    private final List<Option> optionList;

    public Question(String id, String text, Set<String> answerSet) {
        this.id = id;
        this.text = text;
        this.answerSet = answerSet;
        this.optionList = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public List<Option> getOptionList() {
        return optionList;
    }

    public Set<String> getAnswerSet() {
        return answerSet;
    }

}
