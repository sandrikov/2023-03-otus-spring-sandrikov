package ru.otus.homework.books.mappers;

import ru.otus.homework.books.domain.Comment;
import ru.otus.homework.books.dto.CommentDto;

public interface CommentMapper {
    Comment toEntity(CommentDto commentDto);

    CommentDto toDto(Comment comment);

    void partialUpdate(CommentDto dto, Comment target);
}
