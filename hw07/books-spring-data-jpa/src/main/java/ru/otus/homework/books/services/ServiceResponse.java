package ru.otus.homework.books.services;

import lombok.Data;

@Data
public class ServiceResponse<T> {

    private final Status status;

    private final T data;

    private final String message;

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
