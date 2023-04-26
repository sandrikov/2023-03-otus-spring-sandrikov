package ru.otus.homework.quiz.services.parser;

public class ReplyParserUtils {

    public static String checkNotBlank(String input) throws ReplyException {
        if (input != null && !input.isBlank()) {
            return input;
        }
        throw new ReplyException();
    }

}
