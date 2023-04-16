package ru.otus.homework.quiz.model;

import java.util.Date;
import java.util.List;

public record QuizResult(Quiz quiz, Student student, List<Answer> answers, int score, boolean passed, Date time) {
}
