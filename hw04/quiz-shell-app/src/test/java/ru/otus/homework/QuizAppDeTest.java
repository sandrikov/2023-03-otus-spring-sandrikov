package ru.otus.homework;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.homework.quiz.dao.QuizResultRepository;
import ru.otus.homework.quiz.model.Student;
import ru.otus.homework.quiz.services.QuizService;
import ru.otus.homework.quiz.services.io.IOService;
import ru.otus.homework.quiz.services.io.IOServiceTestImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.annotation.DirtiesContext.MethodMode.BEFORE_METHOD;

@SpringBootTest(properties = "spring.profiles.active=test")
public class QuizAppDeTest {

    @DirtiesContext(methodMode = BEFORE_METHOD)
    @Test
    void testQuizService(@Autowired QuizService quizService, @Autowired QuizResultRepository dao) {
        quizService.start();
        var results = dao.getResults(new Student("Hans", "Andersen"));
        assertEquals(1, results.size(), "Expected the result of one test");
        var result = results.get(0);
        assertEquals(3, result.answers().size(), "Number of answers");
        assertTrue(result.answers().get(0).correct(), "1st answer is wrong");
        assertTrue(result.answers().get(1).correct(), "2nd answer is right");
        assertTrue(result.answers().get(2).correct(), "3rd answer is right");
        assertEquals(3, result.score(), "Result score");
        assertTrue(result.passed(), "Quiz failed");
    }

    @TestConfiguration
    static class QuizAppTestContext {

        @Bean(name = "ioService")
        IOService getIOServiceEN(@Value("${application.io.silent}") boolean silent) {
            return new IOServiceTestImpl(silent,
                    "Hans Andersen",  // student right input
                    // 1st question: choose-one-option
                    "2",              // right answer
                    // 2nd question: free text
                    "Kat",            // right answer
                    // 3rd question: select-one-or-more-options
                    "3 1"             // right answer
            );
        }
    }
}
