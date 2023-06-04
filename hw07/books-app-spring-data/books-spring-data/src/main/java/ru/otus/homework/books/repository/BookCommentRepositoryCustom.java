package ru.otus.homework.books.repository;

import ru.otus.homework.books.domain.Book;
import ru.otus.homework.books.domain.Comment;

import java.util.Optional;

public interface BookCommentRepositoryCustom {
    Optional<Comment> findCommentById(long id);

    Comment saveComment(Book book, Comment comment);

    void deleteComment(Book book, Comment comment);
}
