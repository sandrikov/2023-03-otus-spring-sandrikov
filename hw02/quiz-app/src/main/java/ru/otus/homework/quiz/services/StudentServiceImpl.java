package ru.otus.homework.quiz.services;

import org.springframework.stereotype.Service;

import ru.otus.homework.quiz.dao.StudentRepository;
import ru.otus.homework.quiz.model.Student;

@Service
public class StudentServiceImpl implements StudentService {

	private final StudentRepository repository;

	public StudentServiceImpl(StudentRepository repository) {
		this.repository = repository;
	}

	@Override
	public Student getOrCreateStudent(String firstName, String surname) {
		var student = repository.findByNames(firstName, surname);
		if (student != null) {
			return student;
		}
		student = new Student(firstName, surname);
		repository.save(student);
		return student;
	}

}
