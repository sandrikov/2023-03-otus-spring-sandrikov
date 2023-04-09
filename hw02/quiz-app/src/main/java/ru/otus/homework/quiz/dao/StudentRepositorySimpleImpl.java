package ru.otus.homework.quiz.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import ru.otus.homework.quiz.model.Student;

@Repository
public class StudentRepositorySimpleImpl implements StudentRepository {

	private final Map<String, Student> students;

	public StudentRepositorySimpleImpl() {
		this.students = new HashMap<>();
	}

	@Override
	public Student findByNames(String firstName, String surname) {
		return students.get(createKey(firstName, surname));
	}

	@Override
	public void save(Student student) {
		var key = createKey(student.firstName(), student.surname());
		students.put(key, student);
	}

	private String createKey(String firstName, String surname) {
		return surname + "|" + firstName;
	}
}
