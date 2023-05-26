package ru.otus.homework.books.dao;

import ru.otus.homework.books.model.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreRepository {
    int count();

    void save(Genre genre);

    Genre findById(long id);

    Optional<Genre> findByName(String name);

    List<Genre> findAllUsed();

    List<Genre> findAll();

    void deleteById(long id);

    void delete(Genre genre);
}
