package ru.otus.homework.quiz.dao;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;
import ru.otus.homework.quiz.model.Option;
import ru.otus.homework.quiz.model.Question;
import ru.otus.homework.quiz.model.Quiz;
import ru.otus.homework.quiz.services.io.CsvLoader;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toSet;
import static ru.otus.homework.quiz.model.QuestionAnswerType.OPTION_ID;
import static ru.otus.homework.quiz.model.QuestionAnswerType.OPTION_SET;
import static ru.otus.homework.quiz.model.QuestionAnswerType.TEXT;


@ConditionalOnBean(value = CsvLoader.class)
@Service
public class QuizRepositoryCsvImpl implements QuizRepository {

    private final CsvLoader csvLoader;

    public QuizRepositoryCsvImpl(CsvLoader tableLoader) {
        this.csvLoader = tableLoader;
    }

    @Override
    public Quiz getQuiz() {
        Deque<Question> questions = new ArrayDeque<>();
        var rows = csvLoader.getRows();
        var title = rows.get(0)[0];
        int passingScore = Integer.parseInt(rows.get(0)[1]);
        for (int i = 1; i < rows.size(); i++) {
            var row = rows.get(i);
            switch (row[0]) {
                case "TEXT_QUESTION" -> questions.add(createTextQuestion(row));
                case "CHOOSE_OPTION" -> questions.add(createChooseOptionQuestion(row));
                case "SELECT_OPTIONS" -> questions.add(createSelectOptionsQuestion(row));
                case "OPTION" -> questions.getLast().availableOptions().add(createOption(row));
            }
        }
        return new Quiz(title, passingScore, questions);
    }

    private Option createOption(String[] row) {
        var id = row[1];
        var text = row[2];
        return new Option(id, text);
    }

    private Question createTextQuestion(String[] row) {
        var id = row[1];
        var text = row[2];
        var answerSet = Set.of(row[3]);
        return new Question(id, text, answerSet, emptyList(), TEXT);
    }

    private Question createChooseOptionQuestion(String[] row) {
        var id = row[1];
        var text = row[2];
        var answerSet = Set.of(row[3]);
        return new Question(id, text, answerSet, new ArrayList<>(), OPTION_ID);
    }

    private Question createSelectOptionsQuestion(String[] row) {
        var id = row[1];
        var text = row[2];
        var answerSet = Stream.of(row[3].split("\\s")).collect(toSet());
        return new Question(id, text, answerSet, new ArrayList<>(), OPTION_SET);
    }

}
