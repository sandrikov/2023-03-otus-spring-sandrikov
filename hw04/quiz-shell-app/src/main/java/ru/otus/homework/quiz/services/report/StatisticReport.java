package ru.otus.homework.quiz.services.report;

import ru.otus.homework.quiz.model.QuizResult;

import java.util.List;

public interface StatisticReport {

    String buildReport(List<QuizResult> results);

}
