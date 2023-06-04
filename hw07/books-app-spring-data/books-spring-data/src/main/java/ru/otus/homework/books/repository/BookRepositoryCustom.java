package ru.otus.homework.books.repository;

import ru.otus.homework.books.domain.Author;
import ru.otus.homework.books.domain.Genre;
import ru.otus.homework.books.dto.BookProjection;

import java.util.List;

public interface BookRepositoryCustom {

    List<BookProjection> findAllBookProjections();

    List<BookProjection> findAllBookProjections(Author author, Genre genre, String title);

    long countByAuthorAndGenreAndTitle(Author author, Genre genre, String title);

}
