package ru.otus.homework.books.repository;

import ru.otus.homework.books.domain.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreRepository {
    Genre save(Genre genre);

    Optional<Genre> findById(long id);

    Optional<Genre> findByName(String name);

    List<Genre> findAll();

    void deleteById(long id);

    void delete(Genre genre);

    long count();
}
