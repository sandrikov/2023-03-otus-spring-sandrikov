package ru.otus.homework.quiz.services.io;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.stream.Stream;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.stream.Collectors.joining;
import static org.mockito.Mockito.mock;
import static ru.otus.homework.quiz.services.processors.ConsoleColor.BLACK_UNDERLINED;
import static ru.otus.homework.quiz.services.processors.ConsoleColor.RESET;

public class IOServiceTestImpl extends IOServiceStreams {

    public IOServiceTestImpl(boolean silent, String... input) {
        super(silent ? mock(PrintStream.class) : System.out, input2stream(input));
    }

    private static InputStream input2stream(String... inputChain) {
        var answers = Stream.of(inputChain).collect(joining("\n", "", "\n"));
        return new ByteArrayInputStream(answers.getBytes(UTF_8));
    }

    @Override
    public String readStringWithPrompt(String prompt) {
        var response = super.readStringWithPrompt(prompt);
        outputString("> " + BLACK_UNDERLINED + response + RESET);
        return response;
    }

}
