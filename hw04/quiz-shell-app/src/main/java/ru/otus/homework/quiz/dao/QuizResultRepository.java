package ru.otus.homework.quiz.dao;

import ru.otus.homework.quiz.model.QuizResult;
import ru.otus.homework.quiz.model.Student;

import java.util.List;

public interface QuizResultRepository {

    void save(QuizResult result);

    List<QuizResult> getResults();

    default List<QuizResult> getResults(Student student) {
        return getResults().stream().filter(t -> t.student().equals(student)).toList();
    }

}
