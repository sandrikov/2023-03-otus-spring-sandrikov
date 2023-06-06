package ru.otus.homework.books.repository;

import ru.otus.homework.books.domain.Book;
import ru.otus.homework.books.domain.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {
    Comment save(Comment comment);

    Optional<Comment> findById(long id);

    void delete(Comment comment);

    void deleteAllByBooksInBatch(List<Book> book);

    void deleteAllInBatch();

}
