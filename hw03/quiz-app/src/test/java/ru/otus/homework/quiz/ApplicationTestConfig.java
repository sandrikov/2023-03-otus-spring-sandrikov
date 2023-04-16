package ru.otus.homework.quiz;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import ru.otus.homework.quiz.services.IOService;
import ru.otus.homework.quiz.services.IOServiceStreams;

import java.io.ByteArrayInputStream;
import java.io.PrintStream;
import java.util.stream.Stream;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.stream.Collectors.joining;

@EnableAspectJAutoProxy
@Configuration
public class ApplicationTestConfig {

    @Bean(name = "ioService")
    IOService getIOService() {
        var answers = Stream.of(SpringBootQuizTest.INPUT).collect(joining("\n", "", "\n"));
        var in = new ByteArrayInputStream(answers.getBytes(UTF_8));
        var out = Mockito.mock(PrintStream.class);
        // var out = System.out;
        return new IOServiceStreams(out, in);
    }

}
