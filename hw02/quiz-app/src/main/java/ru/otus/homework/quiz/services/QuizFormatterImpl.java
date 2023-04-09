package ru.otus.homework.quiz.services;

import java.util.Formatter;

import org.springframework.stereotype.Service;

import ru.otus.homework.quiz.model.Question;
import ru.otus.homework.quiz.model.Quiz;
import ru.otus.homework.quiz.model.QuizResult;

@Service
public class QuizFormatterImpl implements QuizFormatter {

	@Override
	public String getHeader(Quiz quiz) {
		var sb = new StringBuilder();

		var lineQuiz = "*   Welcome to Quiz: " + quiz.name() + "    *";
		var split = "*".repeat(lineQuiz.length());

		sb.append('\n').append(split);
		sb.append('\n').append(lineQuiz);
		sb.append('\n').append(split);
		sb.append('\n');

		return sb.toString();
	}

	@Override
	public String getErrorMessage(ReplyException ex) {
		if (ex.getMessage() == null) {
			return null;
		}
		if (ex.getInfo() == null) {
			return "Error! " + ex.getMessage();
		}
		return "Error! " + ex.getMessage() + ": " + ex.getInfo();
	}

	@Override
	public String getQuestionPrompt(Question question) {
		try (Formatter formatter = new Formatter()) {
			formatter.format("%-2s %-35s:", question.id(), question.text());
			question.availableOptions().stream()
				.forEach(o -> formatter.format("%n   %-2s %-35s", o.id(), o.text()));
			return formatter.toString();
		}
	}

	@Override
	public String getResultMessage(QuizResult result) {
		var sb = new StringBuilder();
		var maxPossibleScore = result.quiz().questions().size();
		sb.append("The result of ").append(result.student().surname()).append(" ")
				.append(result.student().firstName().charAt(0)).append(".: ")
				.append(result.score()).append(" of ").append(maxPossibleScore);
		if (result.passed()) {
			return sb.append("\nQUIZ PASSED!").toString();
		}
		return sb.append("\nQUIZ FAILED!\nPassing score: ").append(result.quiz().passingScore()).toString();
	}

	@Override
	public String getStudentPrompt() {
		return "Enter first name and surname: ";
	}

	@Override
	public String getExpectedOneOptionMessage() {
		return "Choose only one option code";
	}

	@Override
	public String getInvalidOptionMessage() {
		return "Wrong option code";
	}

	@Override
	public String getMustBeUniqueMessage() {
		return "Non unique option code";
	}

}
