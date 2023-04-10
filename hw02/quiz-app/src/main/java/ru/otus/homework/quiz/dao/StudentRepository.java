package ru.otus.homework.quiz.dao;

import ru.otus.homework.quiz.model.Student;

public interface StudentRepository {

	Student findByNames(String firstName, String surname);

	void save(Student student);
}
