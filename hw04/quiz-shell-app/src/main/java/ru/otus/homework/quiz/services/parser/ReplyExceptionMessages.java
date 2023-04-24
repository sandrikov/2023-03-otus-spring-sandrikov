package ru.otus.homework.quiz.services.parser;

public interface ReplyExceptionMessages {
    String getExpectedOneOptionMessage();

    String getInvalidOptionMessage(String optionId);

    String getMustBeUniqueMessage(String optionId);
}
