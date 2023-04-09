package ru.otus.homework;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import ru.otus.homework.quiz.AppConfig;
import ru.otus.homework.quiz.services.QuizService;

public class Main {

	public static void main(String[] args) {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

		var service = context.getBean(QuizService.class);

		service.run();

	}
}
