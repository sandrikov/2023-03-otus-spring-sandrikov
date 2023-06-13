package ru.otus.homework.books.services;

import ru.otus.homework.books.dto.CommentDto;

public interface CommentService {

    ServiceResponse<CommentDto> addComment(long bookId, CommentDto comment);

    ServiceResponse<CommentDto> modifyComment(CommentDto comment);

    ServiceResponse<CommentDto> deleteComment(long commentId);

}
