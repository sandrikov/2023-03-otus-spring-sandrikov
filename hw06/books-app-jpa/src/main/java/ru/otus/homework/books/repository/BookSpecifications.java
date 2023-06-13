package ru.otus.homework.books.repository;

import org.springframework.data.jpa.domain.Specification;
import ru.otus.homework.books.domain.Author;
import ru.otus.homework.books.domain.Book;
import ru.otus.homework.books.domain.Genre;

public class BookSpecifications {
    public static Specification<Book> authorEquals(Author author) {
        if (author == null) {
            return null;
        }
        return (root, query, cb) -> cb.equal(root.get("author"), author);
    }

    public static Specification<Book> genreEquals(Genre genre) {
        if (genre == null) {
            return null;
        }
        return (root, query, cb) -> cb.equal(root.get("genre"), genre);
    }

    public static Specification<Book> titleEquals(String title) {
        if (title == null) {
            return null;
        }
        return (root, query, cb) -> cb.equal(root.get("title"), title);
    }
}
