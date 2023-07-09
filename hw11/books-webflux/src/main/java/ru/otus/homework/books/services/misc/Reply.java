package ru.otus.homework.books.services.misc;

import java.util.function.Function;

import static ru.otus.homework.books.services.misc.Reply.Status.ERROR;

public record Reply<T>(Status status, T data, String message) {

    public <E extends Throwable> T orElseThrow(Function<String, E> constructor) throws E {
        if (status == ERROR) {
            throw constructor.apply(message);
        }
        return data();
    }

    public static <T> Reply<T> done(T data) {
        return done(data, null);
    }

    public static <T> Reply<T> done(T data, String message) {
        return new Reply<>(Status.OK, data, message);
    }

    public static <T> Reply<T> error(String message) {
        return new Reply<>(Status.ERROR, null, message);
    }

    public enum Status {
        OK,
        ERROR
    }
}
