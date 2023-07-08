package ru.otus.homework.books.services;

import lombok.Data;

import java.util.function.Function;

import static ru.otus.homework.books.services.ServiceResponse.Status.ERROR;

@Data
public class ServiceResponse<T> {

    private final Status status;

    private final T data;

    private final String message;

    public <E extends Throwable> T orElseThrow(Function<String, E> constructor) throws E {
        if (status ==  ERROR) {
            throw constructor.apply(message);
        }
        return getData();
    }

    public static <T> ServiceResponse<T> done(T data) {
        return done(data, null);
    }

    public static <T> ServiceResponse<T> done(T data, String message) {
        return new ServiceResponse<>(Status.OK, data, message);
    }

    public static <T> ServiceResponse<T> error(String message) {
        return new ServiceResponse<>(Status.ERROR, null, message);
    }

    public enum Status {
        OK,
        ERROR
    }
}
