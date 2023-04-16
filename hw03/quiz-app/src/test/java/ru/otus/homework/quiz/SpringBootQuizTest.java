package ru.otus.homework.quiz;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import ru.otus.homework.quiz.dao.QuizResultRepository;
import ru.otus.homework.quiz.model.Student;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(properties = "spring.profiles.active=test")
@Configuration
class SpringBootQuizTest {

    static final String[] INPUT = {
            "Smith",          // student only surname
            "Hans Andersen",  // student right input
            // 1st question: choose-one-option
            "",               // empty input
            "9",              // answer wrong option
            "2 4",            // answer more than one option
            "3",              // wrong answer
            // 2nd question: free text
            "  \t",           // blank input
            "Kat",            // right answer
            // 3rd question: select-one-or-more-options
            "\t ",            // blank input
            "2 2",            // not unique
            "3 1"             // right answer
    };

    @Test
    void test(@Autowired QuizResultRepository dao) {
        var student = new Student("Hans", "Andersen");
        var results = dao.getResults(student);
        assertEquals(1, results.size(), "Expected the result of one test");
        var result = results.get(0);
        assertEquals(3, result.answers().size(), "Number of answers");
        assertFalse(result.answers().get(0).correct(), "1st answer is wrong");
        assertTrue(result.answers().get(1).correct(), "2nd answer is right");
        assertTrue(result.answers().get(2).correct(), "3rd answer is right");
        assertEquals(2, result.score(), "Result score");
        assertFalse(result.passed(), "Quiz failed");
    }

}
