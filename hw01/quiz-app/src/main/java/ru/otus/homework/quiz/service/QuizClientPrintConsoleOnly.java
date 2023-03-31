package ru.otus.homework.quiz.service;

import ru.otus.homework.quiz.domain.Answer;
import ru.otus.homework.quiz.domain.Question;
import ru.otus.homework.quiz.domain.Quiz;
import ru.otus.homework.quiz.domain.QuizResult;

public class QuizClientPrintConsoleOnly implements QuizClient {
    @Override
    public QuizResult doTest(Quiz quiz) {

        System.out.println("\n* * * * * " + quiz.name().toUpperCase() + " * * * * *");
        System.out.println("\nEnter you name: ");

        // TODO Scanner reader = new Scanner(System.in);
        System.out.println("> Ivan"); // TODO reader.next() and check
        String studentName = "Ivan";

        QuizResult result = new QuizResult(studentName);

        for (var q : quiz.questionDeque()) {
            result.getAnswerList().add(askQuestion(q));
        }

        return result;
    }

    private Answer askQuestion(Question q) {
        System.out.printf("\n%-2s %-35s:\n", q.getId(), q.getText());
        if (!q.getOptionList().isEmpty()) {
            for (var o : q.getOptionList()) {
                System.out.printf("   %-2s %-35s\n", o.id(), o.text());
            }
        }
        var answer = new Answer(q);

        System.out.printf("> %s%n", String.join(" ", q.getAnswerSet())); // TODO reader.next() and check
        answer.getAnswerSet().addAll(q.getAnswerSet());
        return answer;
    }
}
