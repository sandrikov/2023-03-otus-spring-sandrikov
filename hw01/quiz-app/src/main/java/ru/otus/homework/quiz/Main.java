package ru.otus.homework.quiz;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.otus.homework.quiz.service.QuizClient;
import ru.otus.homework.quiz.service.QuizService;

public class Main {

    public static void main(String[] args) {
        QuizService service;
        QuizClient client;
        try (var context = new ClassPathXmlApplicationContext("/spring-context.xml")) {
            service = context.getBean(QuizService.class);
            client = context.getBean(QuizClient.class);
        }

        var quizTask = service.getQuiz();

        var quizResult = client.doTest(quizTask);

        System.out.print("\nThe result of " + quizResult.getStudent() +
                " is : " + quizResult.score() + " of " + quizTask.questionDeque().size());

        // TODO save quizResult
    }
}
