package ru.otus.homework.books.repository;

import org.springframework.data.repository.CrudRepository;
import ru.otus.homework.books.domain.Comment;

interface CommentRepository extends CrudRepository<Comment, Long> {

}
