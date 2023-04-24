package ru.otus.homework.quiz.model;

import java.util.List;
import java.util.Set;

public record Question(String id, String text, Set<String> rightAnswer, List<Option> availableOptions,
                       QuestionAnswerType answerType) {
}
