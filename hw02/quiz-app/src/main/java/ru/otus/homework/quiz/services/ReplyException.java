package ru.otus.homework.quiz.services;

public class ReplyException extends Exception {
	private static final long serialVersionUID = -1967783222036215235L;

	private final String info;

	public ReplyException() {
		this(null);
	}

	public ReplyException(String message) {
		this(message, (String)null);
	}

	public ReplyException(String message, String info) {
		super(message);
		this.info = info;
	}

	public ReplyException(String message, Throwable cause) {
		super(message, cause);
		this.info = null;
	}

	public String getInfo() {
		return info;
	}

}
