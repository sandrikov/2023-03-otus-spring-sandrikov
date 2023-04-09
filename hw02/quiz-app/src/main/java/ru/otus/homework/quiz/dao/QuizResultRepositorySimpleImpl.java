package ru.otus.homework.quiz.dao;

import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.stereotype.Repository;

import ru.otus.homework.quiz.model.QuizResult;

@Repository
public class QuizResultRepositorySimpleImpl implements QuizResultRepository {

	private final Set<QuizResult> results;

	public QuizResultRepositorySimpleImpl() {
		this.results = new LinkedHashSet<>();
	}

	@Override
	public void save(QuizResult result) {
		results.add(result);
	}

}
