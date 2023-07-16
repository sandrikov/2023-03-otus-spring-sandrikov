package ru.otus.homework.books.rest.misc;

import java.io.Serial;

public class BookAppException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -6630996887042987389L;

    public BookAppException(String message) {
        super(message);
    }

}
