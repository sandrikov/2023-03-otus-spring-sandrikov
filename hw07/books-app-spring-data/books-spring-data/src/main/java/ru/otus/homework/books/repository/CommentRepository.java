package ru.otus.homework.books.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.otus.homework.books.domain.Comment;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    long countByBookId(long bookId);

    List<Comment> findAllByBookIdIn(Collection<Long> book_id);

    @Query("select c.book.id, count(c) from Comment c where c.book.id in (:bookIds) group by c.book.id")
    Stream<Object[]> countGroupByBookId(Collection<Long> bookIds);
}
