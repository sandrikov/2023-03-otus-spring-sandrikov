package ru.otus.homework.quiz.configs;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.homework.quiz.services.io.IOService;
import ru.otus.homework.quiz.services.io.IOServiceStreams;


@Configuration
public class ApplicationConfig {

    @ConditionalOnProperty(name = "application.io.input", havingValue = "system")
    @Bean
    IOService getIOService() {
        return new IOServiceStreams(System.out, System.in);
    }

}

