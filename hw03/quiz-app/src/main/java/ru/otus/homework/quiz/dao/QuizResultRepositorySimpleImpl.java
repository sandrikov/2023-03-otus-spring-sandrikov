package ru.otus.homework.quiz.dao;

import org.springframework.stereotype.Component;
import ru.otus.homework.quiz.model.QuizResult;
import ru.otus.homework.quiz.model.Student;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Component
public class QuizResultRepositorySimpleImpl implements QuizResultRepository {

    private final Set<QuizResult> results;

    public QuizResultRepositorySimpleImpl() {
        this.results = new LinkedHashSet<>();
    }

    @Override
    public void save(QuizResult result) {
        results.add(result);
    }

    @Override
    public List<QuizResult> getResults(Student student) {
        return results.stream().filter(t -> student.equals(t.student())).toList();
    }

}
