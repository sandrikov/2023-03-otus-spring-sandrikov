package ru.otus.homework.books.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.homework.books.domain.Genre;
import ru.otus.homework.books.mappers.GenreMapper;
import ru.otus.homework.books.repository.GenreRepository;
import ru.otus.homework.books.rest.dto.GenreDto;

@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    private final GenreMapper genreMapper;

    @Override
    public Flux<GenreDto> listGenres() {
        return genreRepository.findAll().map(genreMapper::toDto);
    }

    @Override
    public Mono<GenreDto> getGenre(long id) {
        return genreRepository.findById(id).map(genreMapper::toDto);
    }

    @Override
    public  Mono<GenreDto> createGenre(Mono<GenreDto> genreDto) {
        return genreDto.map(genreMapper::toEntity)
                .flatMap(genre -> genreRepository.save(genre).map(genreMapper::toDto));
    }

    @Override
    public Mono<GenreDto> updateGenre(long id, Mono<GenreDto> genreDto) {
        return genreRepository.findById(id)
                .zipWith(genreDto)
                .map(t -> genreMapper.partialUpdate(t.getT2(), t.getT1()))
                .flatMap(genre -> genreRepository.save(genre).map(genreMapper::toDto));
    }

    @Override
    public Mono<GenreDto> deleteGenre(long id) {
        return genreRepository.findById(id)
                .flatMap(s -> genreRepository.delete(s)
                        .then(Mono.just(genreMapper.toDto(s))));
    }

    @Override
    public Mono<Genre> findGenre(Long id) {
        return genreRepository.findById(id);
    }

}
