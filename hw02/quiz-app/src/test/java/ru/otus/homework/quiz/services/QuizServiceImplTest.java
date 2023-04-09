package ru.otus.homework.quiz.services;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.stream.Collectors.joining;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.context.annotation.FilterType.ASSIGNABLE_TYPE;

import java.io.ByteArrayInputStream;
import java.io.PrintStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import ru.otus.homework.quiz.AppConfig;
import ru.otus.homework.quiz.dao.CsvLoader;
import ru.otus.homework.quiz.dao.ResourceCsvLoader;
import ru.otus.homework.quiz.model.Student;

@Configuration
@ComponentScan(basePackages = "ru.otus.homework.quiz",
	excludeFilters = @Filter(type = ASSIGNABLE_TYPE, value = AppConfig.class))
@PropertySource("classpath:apptest.properties")
class QuizServiceImplTest {


	@Bean
	IOService getIOService() throws Exception {
		var in = createInputStreamEmulator(
				"Smith",		// student
				"Tom Smith",	// student right input
				// 1st question: choose-one-option
				"",				// empty input
				"9",			// answer wrong option
				"2 4",			// answer more than one option
				"3",			// wrong answer
				// 2nd question: free text
				"  \t",			// blank input
				"Cat",			// right answer
				// 3rd question: select-one-or-more-options
				" \t",			// blank input
				"2 2",			// not unique
				"3 1"			// right answer
				);
		PrintStream out = Mockito.mock(PrintStream.class);
		return new IOServiceStreams(out, in);
	}

	@Bean
	CsvLoader getTableLoader(@Value("${resource.name}") String resourceName) {
		return new ResourceCsvLoader(resourceName);
	}

	@Test
	void testRun() {
		QuizService service;
		try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
				QuizServiceImplTest.class)) {
			service = context.getBean(QuizService.class);
		}
		var result = service.run();
		assertEquals(new Student("Tom", "Smith"), result.student());
		assertEquals(3, result.answers().size(), "Number of answers");
		assertEquals(2, result.score(), "Result score");
		assertFalse(result.answers().get(0).correct(), "1st answer is wrong");
		assertTrue(result.answers().get(1).correct(), "2nd answer is right");
		assertTrue(result.answers().get(2).correct(), "3rd answer is right");
		assertFalse(result.passed(), "Quiz failed");
	}

	private ByteArrayInputStream createInputStreamEmulator(String... inputLines) {
		var input = Stream.of(inputLines).collect(joining("\n", "", "\n")).getBytes(UTF_8);
		return new ByteArrayInputStream(input);
	}

}
