package ru.otus.homework.books.repository;

import lombok.val;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import ru.otus.homework.books.domain.Author;
import ru.otus.homework.books.domain.Book;
import ru.otus.homework.books.domain.Genre;
import ru.otus.homework.books.rest.dto.BookProjection;

import java.util.List;

import static org.springframework.data.domain.ExampleMatcher.matchingAll;

public interface BookRepositoryCustom {

    ExampleMatcher EXAMPLE_MATCHER = matchingAll()
            .withIgnoreNullValues().withIgnorePaths("id", "author.name", "genre.name");

    default Example<Book> createExample(Author author, Genre genre, String title) {
        val probe = new Book(title, author, genre);
        return Example.of(probe, EXAMPLE_MATCHER);
    }

    List<BookProjection> findAllBookProjections();

}
