package ru.otus.homework.books.controller;

import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import ru.otus.homework.books.BooksApplication;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@AutoConfigureWebTestClient
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = BooksApplication.class)
@ActiveProfiles("circus-breaker")
public class CircuitBreakerTest {

    @Autowired
    protected CircuitBreakerRegistry circuitBreakerRegistry;

    @Test
    void testStart() throws Exception {
        System.out.println("AllCircuitBreakers: " + circuitBreakerRegistry.getAllCircuitBreakers());
    }

}
