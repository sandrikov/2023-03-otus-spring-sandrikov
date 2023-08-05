package ru.otus.homework.books.mappers;

import org.springframework.stereotype.Service;
import ru.otus.homework.books.domain.Book;
import ru.otus.homework.books.domain.Comment;
import ru.otus.homework.books.dto.CommentDto;

@Service
public class CommentMapperImpl implements CommentMapper {
    @Override
    public Comment toEntity(CommentDto dto, Book book) {
        if (dto == null) {
            return null;
        }
        return new Comment(dto.getId(), dto.getText(), book);
    }

    @Override
    public CommentDto toDto(Comment comment) {
        if (comment == null) {
            return null;
        }
        return new CommentDto(comment.getId(), comment.getText());
    }

    @Override
    public void partialUpdate(CommentDto dto, Comment comment) {
        if (dto == null) {
            return;
        }
        if (dto.getText() != null) {
            comment.setText(dto.getText());
        }
    }
}
