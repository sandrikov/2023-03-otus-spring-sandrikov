package ru.otus.homework.quiz.services.report;

import org.springframework.stereotype.Service;
import ru.otus.homework.quiz.dao.QuizResultRepository;

@Service
public class QuizReportingImpl implements QuizReporting {

    private final StatisticReport statisticReport;

    private final QuizResultRepository quizResultRepository;

    public QuizReportingImpl(StatisticReport statisticReport, QuizResultRepository quizResultRepository) {
        this.statisticReport = statisticReport;
        this.quizResultRepository = quizResultRepository;
    }

    @Override
    public String getStatistic() {
        var results = quizResultRepository.getResults();
        return statisticReport.buildReport(results);
    }
}
