package ru.otus.homework.books.services;

import ru.otus.homework.books.rest.dto.CommentDto;

import java.util.Collection;
import java.util.Map;

public interface CommentService {

    ServiceResponse<CommentDto> addComment(long bookId, CommentDto comment);

    ServiceResponse<CommentDto> modifyComment(CommentDto comment);

    ServiceResponse<CommentDto> deleteComment(long commentId);

    void deleteComments(Collection<Long> bookIds);

    long countByBookId(Long bookId);

    Map<Long, Long> countGroupByBookId(Collection<Long> bookIds);

}
