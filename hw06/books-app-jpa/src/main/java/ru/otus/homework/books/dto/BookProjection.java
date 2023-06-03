package ru.otus.homework.books.dto;

public record BookProjection(
        long id,
        String title,
        AuthorDto author,
        GenreDto genre,
        long commentCount) {
}
