package ru.otus.homework.books.services.misc;

import java.io.Serial;

public class EntityNotFoundException extends Exception {
    @Serial
    private static final long serialVersionUID = -1474396923832934996L;

    public EntityNotFoundException(String message) {
        super(message);
    }
}
