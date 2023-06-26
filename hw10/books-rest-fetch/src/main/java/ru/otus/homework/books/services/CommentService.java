package ru.otus.homework.books.services;

import ru.otus.homework.books.rest.dto.CommentDto;
import ru.otus.homework.books.services.misc.Reply;

import java.util.Collection;
import java.util.Map;

public interface CommentService {

    Reply<CommentDto> addComment(long bookId, CommentDto comment);

    Reply<CommentDto> modifyComment(CommentDto comment);

    Reply<CommentDto> deleteComment(long commentId);

    void deleteComments(Collection<Long> bookIds);

    long countByBookId(Long bookId);

    Map<Long, Long> countGroupByBookId(Collection<Long> bookIds);

}
