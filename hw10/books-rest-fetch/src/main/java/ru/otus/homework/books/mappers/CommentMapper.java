package ru.otus.homework.books.mappers;

import ru.otus.homework.books.domain.Book;
import ru.otus.homework.books.domain.Comment;
import ru.otus.homework.books.rest.dto.CommentDto;

public interface CommentMapper {
    Comment toEntity(CommentDto commentDto, Book book);

    CommentDto toDto(Comment comment);

    void partialUpdate(CommentDto dto, Comment target);
}
