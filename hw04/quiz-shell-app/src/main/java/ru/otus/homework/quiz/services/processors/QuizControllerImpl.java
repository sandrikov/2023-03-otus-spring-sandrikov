package ru.otus.homework.quiz.services.processors;

import org.springframework.stereotype.Controller;
import ru.otus.homework.quiz.model.Answer;
import ru.otus.homework.quiz.model.Question;
import ru.otus.homework.quiz.model.Quiz;
import ru.otus.homework.quiz.model.QuizResult;
import ru.otus.homework.quiz.services.io.IOService;
import ru.otus.homework.quiz.services.parser.QuestionParserService;
import ru.otus.homework.quiz.services.parser.ReplyException;
import ru.otus.homework.quiz.services.parser.ReplyParser;

import static ru.otus.homework.quiz.services.parser.ReplyParserUtils.checkNotBlank;

@Controller
public class QuizControllerImpl implements QuizController {

    private final IOService ioService;

    private final QuestionParserService questionParserService;

    private final QuizFormatter quizFormatter;

    public QuizControllerImpl(IOService ioService, QuestionParserService questionParserService,
                              QuizFormatter quizFormatter) {
        this.ioService = ioService;
        this.questionParserService = questionParserService;
        this.quizFormatter = quizFormatter;
    }

    @Override
    public void showWelcome(Quiz quiz) {
        var banner = quizFormatter.getHeader(quiz);
        ioService.outputString(banner);
    }

    @Override
    public String[] requestStudentNames() {
        var prompt = quizFormatter.getStudentPrompt();
        return request(prompt, this::getTwoNames);
    }

    @Override
    public Answer requestAnswer(Question question) {
        var prompt = quizFormatter.getQuestionPrompt(question);
        var replyParser = questionParserService.getReplyParser(question);
        return request(prompt, replyParser);
    }

    @Override
    public void showResult(QuizResult quizResult) {
        ioService.outputString(quizFormatter.getResultMessage(quizResult));
    }

    public <T> T request(String prompt, ReplyParser<T> replyParser) {
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

    private String[] getTwoNames(String input) throws ReplyException {
        var words = checkNotBlank(input).split("\\s");
        if (words.length == 2) {
            return words;
        }
        throw new ReplyException();
    }
}
