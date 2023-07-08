package ru.otus.homework.books.services;

import ru.otus.homework.books.domain.Genre;
import ru.otus.homework.books.dto.GenreDto;
import ru.otus.homework.books.services.misc.EntityNotFoundException;

import java.util.List;

public interface GenreService {
    ServiceResponse<List<GenreDto>> listGenres();

    ServiceResponse<GenreDto> getGenre(long id);

    ServiceResponse<GenreDto> findGenre(String name);

    ServiceResponse<GenreDto> createGenre(String name);

    ServiceResponse<GenreDto> renameGenre(long id, String name);

    ServiceResponse<GenreDto> deleteGenre(long id);

    Genre findGenre(Long genreId) throws EntityNotFoundException;
}
