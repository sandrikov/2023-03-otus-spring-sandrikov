package ru.otus.homework.books.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {

    private Long id;

    private String title;

    private AuthorDto author;

    private GenreDto genre;

    public BookDto(String title, AuthorDto author, GenreDto genre) {
        this(null, title, author, genre);
    }

}
