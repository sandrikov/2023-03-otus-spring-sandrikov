package ru.otus.homework.books.rest.misc;

import lombok.val;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class BookAppAdvice {

    @ExceptionHandler(BookAppException.class)
    public ResponseEntity<Response> handleException(BookAppException e) {
        val response = new Response(e.getMessage());
        return ResponseEntity.internalServerError().body(response);
    }

    public record Response (String message) {
    }
}
