package ru.otus.homework.quiz.services;

import org.springframework.stereotype.Service;
import ru.otus.homework.quiz.dao.QuizRepository;
import ru.otus.homework.quiz.dao.QuizResultRepository;
import ru.otus.homework.quiz.dao.StudentRepository;
import ru.otus.homework.quiz.model.Answer;
import ru.otus.homework.quiz.model.Quiz;
import ru.otus.homework.quiz.model.QuizResult;
import ru.otus.homework.quiz.model.Student;
import ru.otus.homework.quiz.services.processors.QuizController;

import java.util.Date;
import java.util.List;

@Service
public class QuizServiceImpl implements QuizService {

    private final QuizRepository quizRepository;

    private final QuizResultRepository quizResultRepository;

    private final StudentRepository studentRepository;

    private final QuizController quizController;

    public QuizServiceImpl(QuizRepository quizRepository, QuizResultRepository quizResultRepository,
                           StudentRepository studentRepository, QuizController quizController) {
        this.quizRepository = quizRepository;
        this.quizResultRepository = quizResultRepository;
        this.studentRepository = studentRepository;
        this.quizController = quizController;
    }


    @Override
    public void start() {
        var quiz = quizRepository.getQuiz();
        quizController.showWelcome(quiz);
        var student = registrateStudent();
        var answers = quiz.questions().stream()
                .map(quizController::requestAnswer).toList();
        QuizResult result = saveResult(quiz, student, answers);
        quizController.showResult(result);
    }

    private QuizResult saveResult(Quiz quiz, Student student, List<Answer> answers) {
        var score = answers.stream().filter(Answer::correct).count();
        var passed = score >= quiz.passingScore();
        var result = new QuizResult(quiz, student, answers, score, passed, new Date());
        quizResultRepository.save(result);
        return result;
    }

    private Student registrateStudent() {
        var studentNames = quizController.requestStudentNames();
        return getOrCreateStudent(studentNames[0], studentNames[1]);
    }

    private Student getOrCreateStudent(String firstName, String surname) {
        var student = studentRepository.findByNames(firstName, surname);
        if (student != null) {
            return student;
        }
        student = new Student(firstName, surname);
        studentRepository.save(student);
        return student;
    }
}
