package ru.otus.homework.books.services;

import ru.otus.homework.books.domain.Genre;
import ru.otus.homework.books.rest.dto.GenreDto;
import ru.otus.homework.books.services.misc.Reply;

import java.util.List;

public interface GenreService {

    Reply<List<GenreDto>> listGenres();

    Reply<GenreDto> getGenre(long id);

    Reply<GenreDto> findGenre(String name);

    Reply<GenreDto> createGenre(String name);

    Reply<GenreDto> renameGenre(long id, String name);

    Reply<GenreDto> deleteGenre(long id);

    Genre findGenre(Long genreId);

}
