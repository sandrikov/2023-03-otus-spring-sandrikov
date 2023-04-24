package ru.otus.homework.quiz.services.parser;

import ru.otus.homework.quiz.model.Answer;
import ru.otus.homework.quiz.model.Question;

public interface QuestionParserService {

    ReplyParser<Answer> getReplyParser(Question question);
}
