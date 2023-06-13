package ru.otus.homework.books.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.homework.books.domain.Book;
import ru.otus.homework.books.domain.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    long countByBook(Book book);
}
