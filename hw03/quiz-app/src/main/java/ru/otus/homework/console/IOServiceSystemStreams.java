package ru.otus.homework.console;

import org.springframework.stereotype.Component;
import ru.otus.homework.quiz.services.IOServiceStreams;

@Component
public class IOServiceSystemStreams extends IOServiceStreams {

    IOServiceSystemStreams() {
        super(System.out, System.in);
    }

}
