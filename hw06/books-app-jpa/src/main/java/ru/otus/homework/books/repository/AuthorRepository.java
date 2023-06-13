package ru.otus.homework.books.repository;

import ru.otus.homework.books.domain.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository {
    Author save(Author author);

    Optional<Author> findById(long id);

    Optional<Author> findByName(String name);

    List<Author> findAll();

    void deleteById(long id);

    void delete(Author author);

    long count();
}
