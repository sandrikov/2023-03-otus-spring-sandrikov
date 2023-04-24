package ru.otus.homework.quiz.services.localize;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import ru.otus.homework.quiz.model.Option;
import ru.otus.homework.quiz.model.Question;
import ru.otus.homework.quiz.model.Quiz;

import static java.util.stream.Collectors.toSet;

@Aspect
@Component
public class LocalizeQuizAspect {

    private final Localizer localizer;

    public LocalizeQuizAspect(Localizer localizer) {
        this.localizer = localizer;
    }

    @Around("execution(* ru.otus.homework.quiz.configs.AppProps.getQuiz())")
    public Quiz localize(ProceedingJoinPoint joinPoint) throws Throwable {
        var quizTemplate = (Quiz) joinPoint.proceed();
        return localize(localizer, quizTemplate);
    }

    private Quiz localize(Localizer l, Quiz source) {
        var name = l.localize(source.name());
        var questions = source.questions().stream().map(q -> localize(l, q)).toList();
        return new Quiz(name, source.passingScore(), questions);
    }

    private Question localize(Localizer l, Question source) {
        var text = l.localize(source.text());
        var rightAnswer = source.rightAnswer().stream().map(l::localize).collect(toSet());
        var availableOptions = source.availableOptions().stream().map(o -> localize(l, o)).toList();
        return new Question(source.id(), text, rightAnswer, availableOptions, source.answerType());
    }

    private Option localize(Localizer l, Option source) {
        var text = l.localize(source.text());
        return new Option(source.id(), text);
    }
}
