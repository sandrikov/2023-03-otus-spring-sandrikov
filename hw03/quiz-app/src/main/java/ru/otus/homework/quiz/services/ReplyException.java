package ru.otus.homework.quiz.services;

import java.io.Serial;

public class ReplyException extends Exception {
    @Serial
    private static final long serialVersionUID = -1967783222036215235L;

    public ReplyException() {
        super((String) null);
    }

    public ReplyException(String message) {
        super(message);
    }

}
