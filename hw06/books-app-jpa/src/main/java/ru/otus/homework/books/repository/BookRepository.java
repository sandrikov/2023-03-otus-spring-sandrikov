package ru.otus.homework.books.repository;

import ru.otus.homework.books.domain.Author;
import ru.otus.homework.books.domain.Book;
import ru.otus.homework.books.domain.Genre;
import ru.otus.homework.books.dto.BookProjection;

import java.util.List;
import java.util.Optional;

public interface BookRepository {

    long count();

    long countByAuthorAndGenreAndTitle(Author author, Genre genre, String title);

    Book save(Book book);

    Optional<Book> findById(long id);

    Optional<Book> findByTitleAndAuthor(String title, Author author);

    List<Book> findAll();

    List<Book> findAllByAuthorAndGenreAndTitle(Author author, Genre genre, String title);

    void deleteById(long id);

    void delete(Book book);

    int deleteAllInBatch(List<Book> booksToDelete);

    int deleteAllInBatch();

    List<BookProjection> findAllWithStat();

    List<BookProjection> findAllWithStatByAuthorAndGenreAndTitle(Author author, Genre genre, String title);

}
