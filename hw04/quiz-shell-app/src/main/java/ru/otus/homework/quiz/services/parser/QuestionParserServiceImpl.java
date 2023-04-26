package ru.otus.homework.quiz.services.parser;

import org.springframework.stereotype.Service;
import ru.otus.homework.quiz.model.Answer;
import ru.otus.homework.quiz.model.Option;
import ru.otus.homework.quiz.model.Question;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import static java.util.Collections.singleton;
import static java.util.stream.Collectors.toSet;
import static ru.otus.homework.quiz.services.parser.ReplyParserUtils.checkNotBlank;

@Service
public class QuestionParserServiceImpl implements QuestionParserService {

    private final ReplyExceptionMessages exceptionMessages;

    public QuestionParserServiceImpl(ReplyExceptionMessages exceptionMessages) {
        this.exceptionMessages = exceptionMessages;
    }

    @Override
    public ReplyParser<Answer> getReplyParser(Question question) {
        switch (question.answerType()) {
            case TEXT -> {
                return in -> getTextAnswer(in, question);
            }
            case OPTION_ID -> {
                return in -> getChooseOneOption(in, question);
            }
            case OPTION_SET -> {
                return in -> getSelectOptionIds(in, question);
            }
            default ->
                // impossible
                    throw new IllegalArgumentException(question.answerType().name());
        }
    }

    private Answer getTextAnswer(String input, Question question) throws ReplyException {
        var text = checkNotBlank(input);
        return mapToAnswer(question, text);
    }

    private Answer getChooseOneOption(String input, Question question) throws ReplyException {
        var result = checkChooseOneOption(input, question.availableOptions());
        return mapToAnswer(question, result);
    }

    private Answer getSelectOptionIds(String input, Question question) throws ReplyException {
        var result = checkSelectOptionIds(input, question.availableOptions());
        return mapToAnswer(question, result);
    }

    private Set<String> checkChooseOneOption(String input, Collection<Option> options) throws ReplyException {
        var result = checkSelectOptionIds(input, options);
        if (result.size() != 1) {
            throw new ReplyException(exceptionMessages.getExpectedOneOptionMessage());
        }
        return result;
    }

    private Set<String> checkSelectOptionIds(String input, Collection<Option> options) throws ReplyException {
        Set<String> availableOptionIds = options.stream().map(Option::id).collect(toSet());
        Set<String> result = new LinkedHashSet<>(); // order is important
        String[] ids = checkNotBlank(input).split("\\s");
        for (String id : ids) {
            if (!availableOptionIds.contains(id)) {
                throw new ReplyException(exceptionMessages.getInvalidOptionMessage(id));
            }
            if (!result.add(id)) {
                throw new ReplyException(exceptionMessages.getMustBeUniqueMessage(id));
            }
        }
        if (result.isEmpty()) {
            throw new ReplyException();
        }
        return result;
    }

    private Answer mapToAnswer(Question question, String result) {
        return mapToAnswer(question, singleton(result));
    }

    private Answer mapToAnswer(Question question, Set<String> result) {
        var correct = isCorrectAnswer(question, result);
        return new Answer(result, correct);
    }

    private boolean isCorrectAnswer(Question question, Set<String> result) {
        return question.rightAnswer().size() == result.size() && question.rightAnswer().containsAll(result);
    }

}
