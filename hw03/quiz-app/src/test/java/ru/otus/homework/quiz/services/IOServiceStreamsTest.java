package ru.otus.homework.quiz.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.PrintStream;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class IOServiceStreamsTest {

    @Mock
    private PrintStream outputStream;

    @Test
    void testOutputString() {
        var prompt = "Prompt";
        var ioService = new IOServiceStreams(outputStream, System.in);
        ioService.outputString(prompt);
        verify(outputStream, times(1)).println(prompt);
    }

    @Test
    void testReadStringWithPrompt() {
        var source = new ByteArrayInputStream("line1\nline2".getBytes(UTF_8));

        var ioService = new IOServiceStreams(outputStream, source);
        assertEquals("line1", ioService.readStringWithPrompt("Prompt1"));
        assertEquals("line2", ioService.readStringWithPrompt("Prompt2"));
    }

}
