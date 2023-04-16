package ru.otus.homework.quiz.services;

import org.springframework.stereotype.Controller;
import ru.otus.homework.quiz.model.Answer;
import ru.otus.homework.quiz.model.Option;
import ru.otus.homework.quiz.model.Question;
import ru.otus.homework.quiz.model.Quiz;
import ru.otus.homework.quiz.model.QuizResult;
import ru.otus.homework.quiz.model.Student;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import static java.util.Collections.singleton;
import static java.util.stream.Collectors.toSet;

@Controller
public class QuizControllerImpl implements QuizController {

    private final IOService ioService;

    private final StudentService studentService;

    private final QuizFormatter quizFormatter;

    public QuizControllerImpl(IOService ioService, StudentService studentService, QuizFormatter quizFormatter) {
        this.ioService = ioService;
        this.studentService = studentService;
        this.quizFormatter = quizFormatter;
    }

    @Override
    public void start(Quiz quiz) {
        ioService.outputString(quizFormatter.getHeader(quiz));
    }

    @Override
    public Student getStudent() {
        return request(quizFormatter.getStudentPrompt(), this::checkStudentNames);
    }

    @Override
    public Answer getAnswer(Question question) {
        var result = askQuestion(question);
        var correct = isCorrectAnswer(question, result);
        return new Answer(result, correct);
    }

    @Override
    public void showResult(QuizResult quizResult) {
        ioService.outputString(quizFormatter.getResultMessage(quizResult));
    }

    private <T> T request(String prompt, ReplyParser<T> replyParser) {
        var input = ioService.readStringWithPrompt(prompt);
        try {
            return replyParser.parse(input);
        } catch (ReplyException ex) {
            var errorMessage = ex.getMessage();
            if (errorMessage != null) {
                ioService.outputString(errorMessage);
            }
            return request(prompt, replyParser); // recursion
        }
    }

    private boolean isCorrectAnswer(Question question, Set<String> result) {
        return question.rightAnswer().size() == result.size() && question.rightAnswer().containsAll(result);
    }

    private Set<String> askQuestion(Question question) {
        String prompt = quizFormatter.getQuestionPrompt(question);
        switch (question.answerType()) {
            case TEXT -> {
                var answer = request(prompt, this::checkNotBlank);
                return singleton(answer);
            }
            case OPTION_ID -> {
                return request(prompt, t -> checkChooseOneOption(t, question.availableOptions()));
            }
            case OPTION_SET -> {
                return request(prompt, t -> checkSelectOptionIds(t, question.availableOptions()));
            }
            default ->
                // impossible
                    throw new IllegalArgumentException(question.answerType().name());
        }
    }

    private String checkNotBlank(String input) throws ReplyException {
        if (input != null && !input.isBlank()) {
            return input;
        }
        throw new ReplyException();
    }

    private Student checkStudentNames(String input) throws ReplyException {
        String[] words = checkNotBlank(input).split("\\s");
        if (words.length == 2) {
            return studentService.getOrCreateStudent(words[0], words[1]);
        }
        throw new ReplyException();
    }

    private Set<String> checkChooseOneOption(String input, Collection<Option> options) throws ReplyException {
        var result = checkSelectOptionIds(input, options);
        if (result.size() != 1) {
            throw new ReplyException(quizFormatter.getExpectedOneOptionMessage());
        }
        return result;
    }

    private Set<String> checkSelectOptionIds(String input, Collection<Option> options) throws ReplyException {
        Set<String> availableOptionIds = options.stream().map(Option::id).collect(toSet());
        Set<String> result = new LinkedHashSet<>(); // order is important
        String[] ids = checkNotBlank(input).split("\\s");
        for (String id : ids) {
            if (!availableOptionIds.contains(id)) {
                throw new ReplyException(quizFormatter.getInvalidOptionMessage(id));
            }
            if (!result.add(id)) {
                throw new ReplyException(quizFormatter.getMustBeUniqueMessage(id));
            }
        }
        if (result.isEmpty()) {
            throw new ReplyException();
        }
        return result;
    }

}
