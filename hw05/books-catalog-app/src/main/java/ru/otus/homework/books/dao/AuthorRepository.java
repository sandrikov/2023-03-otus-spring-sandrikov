package ru.otus.homework.books.dao;

import ru.otus.homework.books.model.Author;

import java.util.List;
import java.util.Optional;


public interface AuthorRepository {

    int count();

    void save(Author author);

    Author findById(long id);

    Optional<Author> findByName(String name);

    List<Author> findAllUsed();

    List<Author> findAll();

    void deleteById(long id);

    void delete(Author author);

}
