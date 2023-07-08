package ru.otus.homework.books.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import static ru.otus.homework.books.domain.SchemaSqlConstants.MAX_TITLE_LENGTH;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {

    private long id;

    @NotBlank
    @Size(min = 1, max = MAX_TITLE_LENGTH)
    private String title;

    private AuthorDto author;

    private GenreDto genre;

    private List<CommentDto> comments;
}
