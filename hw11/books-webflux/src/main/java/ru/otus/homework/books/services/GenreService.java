package ru.otus.homework.books.services;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.homework.books.domain.Genre;
import ru.otus.homework.books.rest.dto.GenreDto;

public interface GenreService {

    Flux<GenreDto> listGenres();

    Mono<GenreDto> getGenre(long id);

    Mono<GenreDto> createGenre(Mono<GenreDto> genreDto);

    Mono<GenreDto> updateGenre(long id, Mono<GenreDto> genreDto);

    Mono<GenreDto> deleteGenre(long id);

    Mono<Genre> findGenre(Long id);
}
