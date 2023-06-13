package ru.otus.homework.books.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {
    private long id;

    private String title;

    private AuthorDto author;

    private GenreDto genre;

    private List<CommentDto> comments;
}
