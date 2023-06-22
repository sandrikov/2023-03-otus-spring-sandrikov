package ru.otus.homework.books.rest.dto;

public record BookProjection(
        long id,
        String title,
        AuthorDto author,
        GenreDto genre,
        long commentCount) {
}
