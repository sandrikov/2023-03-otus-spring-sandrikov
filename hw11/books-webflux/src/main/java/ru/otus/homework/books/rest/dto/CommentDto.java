package ru.otus.homework.books.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private long id;

    private String text;

    public CommentDto(String text) {
        this.text = text;
    }
}
