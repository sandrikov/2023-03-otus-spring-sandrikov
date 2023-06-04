package ru.otus.homework.books.repository;

import lombok.val;
import ru.otus.homework.books.domain.Book;
import ru.otus.homework.books.domain.Comment;

import java.util.Optional;

public class BookCommentRepositoryCustomImpl implements BookCommentRepositoryCustom {

    private final CommentRepository commentRepository;

    public BookCommentRepositoryCustomImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public Optional<Comment> findCommentById(long id) {
        return commentRepository.findById(id);
    }

    @Override
    public Comment saveComment(Book book, Comment comment) {
        if (comment.getId() <= 0) {
            book.addComment(comment);
        }
        return commentRepository.save(comment);
    }

    @Override
    public void deleteComment(Book book, Comment comment) {
        val existingComment = commentRepository.findById(comment.getId());
        if (existingComment.isPresent()) {
            book.removeComment(comment);
            commentRepository.delete(comment);
        }
    }
}
