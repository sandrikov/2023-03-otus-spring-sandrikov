package ru.otus.homework.books.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotNull
    @Min(0)
    @Max(18)
    private Integer ageLimit;

    private List<CommentDto> comments;
}
