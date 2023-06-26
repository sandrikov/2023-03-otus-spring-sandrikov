package ru.otus.homework.books.mappers;

import ru.otus.homework.books.domain.Genre;
import ru.otus.homework.books.rest.dto.GenreDto;

public interface GenreMapper {
    Genre toEntity(GenreDto genreDto);

    GenreDto toDto(Genre genre);
}
