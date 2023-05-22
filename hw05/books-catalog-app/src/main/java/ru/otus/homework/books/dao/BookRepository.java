package ru.otus.homework.books.dao;

import ru.otus.homework.books.model.Author;
import ru.otus.homework.books.model.Book;
import ru.otus.homework.books.model.Genre;

import java.util.List;
import java.util.Optional;

public interface BookRepository {

    int count();

    void save(Book book);

    Book findById(long id);

    Optional<Book> findByNameAndAuthor(String name, Author author);

    List<Book> findByName(String name);

    List<Book> findByAuthor(Author author);

    List<Book> findByGenre(Genre genre);

    List<Book> findAll();

    void deleteById(long id);

    void delete(Book book);

    int deleteAll();

    int deleteAllByAuthor(Author author);

    int deleteAllByAuthorAndGenre(Author author, Genre genre);

    int deleteAllByGenre(Genre genre);

}
