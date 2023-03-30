package ru.otus.homework.quiz.service;

import static java.util.Collections.singleton;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayDeque;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import ru.otus.homework.quiz.domain.Question;
import ru.otus.homework.quiz.domain.Quiz;

class QuizClientPrintConsoleOnlyTest {

	@Test
	void testDoTest() {
		var quiz = new Quiz("Test",
				new ArrayDeque<>(Arrays.asList(new Question("I)", "Say TRUE", singleton("TRUE")))));
		var client = new QuizClientPrintConsoleOnly();
		var result = client.doTest(quiz);
		assertEquals("Ivan", result.getStudent(), "Student name");
		assertEquals(quiz.questionDeque().size(), result.score(), "All answers must right");
	}

}
