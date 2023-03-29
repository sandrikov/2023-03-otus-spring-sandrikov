package ru.otus.homework.quiz.dao;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import ru.otus.homework.quiz.domain.Option;
import ru.otus.homework.quiz.domain.Question;
import ru.otus.homework.quiz.domain.Quiz;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

public class QuizDaoCsvResource implements QuizDao {

    private final ResourceLoader resourceLoader;

    private final String resourceName;

    public QuizDaoCsvResource(String resourceName, ResourceLoader resourceLoader) {
        this.resourceName = resourceName;
        this.resourceLoader = resourceLoader;
    }

    @Override
    public Quiz loadQuiz() {

        String quizName = resourceName.replace(".csv", "")
                .replace('.', ' ');
        var questionDeque = parseQuestions(readCsv());

        return new Quiz(quizName, questionDeque);
    }

    private Deque<Question> parseQuestions(List<String[]> rows) {
        final Deque<Question> questionDeque = new ArrayDeque<>();
        for (String[] row : rows) {
            var type = row[0];
            var id = row[1];
            var text = row[2];
            switch (type) {
                case "Q" -> {
                    var answerSet = Stream.of(row[3].split("\\s")).collect(toSet());
                    questionDeque.add(new Question(id, text, answerSet));
                }
                case "O" -> questionDeque.getLast().getOptionList().add(new Option(id, text));
            }
        }
        return questionDeque;
    }

    private List<String[]> readCsv() {
        final Resource resource = resourceLoader.getResource("classpath:" + resourceName);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            return reader.lines().map(l -> l.split(";")).collect(toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
