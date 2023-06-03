package ru.otus.homework.books.mappers;

import org.springframework.stereotype.Component;
import ru.otus.homework.books.domain.Comment;
import ru.otus.homework.books.dto.CommentDto;

@Component
public class CommentMapperImpl implements CommentMapper {
    @Override
    public Comment toEntity(CommentDto dto) {
        if (dto == null) {
            return null;
        }
        return new Comment(dto.getId(), dto.getText(), null);
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
        if (dto.getText() != null && !dto.getText().equals(comment.getText())) {
            comment.setText(dto.getText());
        }
    }
}
