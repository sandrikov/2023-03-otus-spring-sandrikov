package ru.otus.homework.quiz.services.report;

import org.springframework.stereotype.Service;
import ru.otus.homework.quiz.model.QuizResult;
import ru.otus.homework.quiz.model.Student;
import ru.otus.homework.quiz.services.localization.Localizer;

import java.util.Comparator;
import java.util.Formatter;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static ru.otus.homework.quiz.services.processors.ConsoleColor.BLUE_BOLD;
import static ru.otus.homework.quiz.services.processors.ConsoleColor.CYAN;
import static ru.otus.homework.quiz.services.processors.ConsoleColor.GREEN_BOLD;
import static ru.otus.homework.quiz.services.processors.ConsoleColor.RED_BOLD;
import static ru.otus.homework.quiz.services.processors.ConsoleColor.RESET;

@Service
public class StatisticReportImpl implements StatisticReport {

    private final Localizer localizer;

    public StatisticReportImpl(Localizer localizer) {
        this.localizer = localizer;
    }

    @Override
    public String buildReport(List<QuizResult> results) {
        if (results.isEmpty()) {
            return localizer.getMessage("quiz.reports.empty");
        }
        var repData = prepareData(results);
        return formatData(repData);
    }

    private SortedMap<Student, List<QuizResult>> prepareData(List<QuizResult> results) {
        var comparator = Comparator.comparing(Student::surname)
                .thenComparing(Student::firstName);
        return results.stream().collect(groupingBy(QuizResult::student,
                () -> new TreeMap<>(comparator), toList()));
    }

    private String formatData(SortedMap<Student, List<QuizResult>> repData) {
        try (var formatter = new Formatter()) {
            formatTitle(formatter);
            for (var e : repData.entrySet()) {
                formatStudent(formatter, e.getKey());
                for (var quizResult : e.getValue()) {
                    formatResult(formatter, quizResult);
                }
            }
            return formatter.toString();
        }
    }

    private void formatTitle(Formatter formatter) {
        formatter.format("%n%s%s%s",
                BLUE_BOLD,
                localizer.getMessage("quiz.reports.title"),
                RESET);
    }

    private void formatStudent(Formatter formatter, Student student) {
        formatter.format("%n%s> %s %s:%s",
                CYAN,
                student.surname(), student.firstName(),
                RESET);
    }

    private void formatResult(Formatter formatter, QuizResult quizResult) {
        var maxPossibleScore = quizResult.quiz().questions().size();
        formatter.format("%n  - %-25s %s%d / %d %-15s %s%tT",
                quizResult.quiz().name(),
                quizResult.passed() ? GREEN_BOLD : RED_BOLD,
                quizResult.score(), maxPossibleScore,
                localizer.getMessage(quizResult.passed() ? "quiz.reports.ok" : "quiz.reports.ko"),
                RESET,
                quizResult.time()
        );
    }

}
