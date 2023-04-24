package ru.otus.homework.quiz.services;

import ru.otus.homework.quiz.model.Student;

public interface StudentService {

    Student getOrCreateStudent(String firstName, String surname);

}
