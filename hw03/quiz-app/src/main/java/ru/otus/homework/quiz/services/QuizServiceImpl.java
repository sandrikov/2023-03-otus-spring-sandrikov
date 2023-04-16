package ru.otus.homework.quiz.services;

import org.springframework.stereotype.Service;
import ru.otus.homework.quiz.dao.QuizRepository;
import ru.otus.homework.quiz.dao.QuizResultRepository;
import ru.otus.homework.quiz.model.Answer;
import ru.otus.homework.quiz.model.QuizResult;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class QuizServiceImpl implements QuizService {

    private final QuizRepository quizRepository;

    private final QuizResultRepository quizResultRepository;

    private final QuizController quizController;

    public QuizServiceImpl(QuizRepository quizRepository, QuizResultRepository quizResultRepository,
                           QuizController quizController) {
        this.quizController = quizController;
        this.quizRepository = quizRepository;
        this.quizResultRepository = quizResultRepository;
    }


    @Override
    public void start() {
        var quiz = quizRepository.getQuiz();
        quizController.start(quiz);
        var student = quizController.getStudent();
        int score = 0;
        List<Answer> answers = new ArrayList<>();
        for (var question : quiz.questions()) {
            var answer = quizController.getAnswer(question);
            if (answer.correct()) {
                score++;
            }
            answers.add(answer);
        }
        var passed = score >= quiz.passingScore();
        var result = new QuizResult(quiz, student, answers, score, passed, new Date());
        quizController.showResult(result);
        quizResultRepository.save(result);
    }
}
