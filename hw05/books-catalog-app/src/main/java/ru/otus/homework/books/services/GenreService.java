package ru.otus.homework.books.services;

import ru.otus.homework.books.model.Genre;

import java.util.List;

public interface GenreService {
    ServiceResponse<List<Genre>> listGenres();

    ServiceResponse<Genre> getGenre(long id);

    ServiceResponse<Genre> findGenre(String name);

    ServiceResponse<Genre> createGenre(String name);

    ServiceResponse<Genre> renameGenre(long id, String name);

    ServiceResponse<Genre> deleteGenre(long id);
}
