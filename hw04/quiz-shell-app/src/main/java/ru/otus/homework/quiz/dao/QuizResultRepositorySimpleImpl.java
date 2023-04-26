package ru.otus.homework.quiz.dao;

import org.springframework.stereotype.Component;
import ru.otus.homework.quiz.model.QuizResult;

import java.util.ArrayList;
import java.util.List;

@Component
public class QuizResultRepositorySimpleImpl implements QuizResultRepository {

    private final List<QuizResult> results;

    public QuizResultRepositorySimpleImpl() {
        this.results = new ArrayList<>();
    }

    @Override
    public void save(QuizResult result) {
        results.add(result);
    }

    @Override
    public List<QuizResult> getResults() {
        return results;
    }

}
