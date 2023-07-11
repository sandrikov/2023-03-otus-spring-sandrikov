package ru.otus.homework.books.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.homework.books.domain.Genre;
import ru.otus.homework.books.mappers.GenreMapper;
import ru.otus.homework.books.repository.GenreRepository;
import ru.otus.homework.books.rest.dto.GenreDto;
import ru.otus.homework.books.rest.misc.BookAppException;

import static ru.otus.homework.books.services.misc.ServiceErrorMessages.getGenreAlreadyExistsMessage;

@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {

    public static final String INTEGRITY_VIOLATION_ERROR = """
            Deletion is not possible: there are genre's books.
            First call: delete-books --genre-id %s
            """;

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

    @Transactional
    @Override
    public  Mono<GenreDto> createGenre(Mono<GenreDto> genreDto) {
        Mono<Genre> genreToCreate = genreDto
                .handle((dto, sink) -> {
                    if (genreRepository.existsByName(dto.getName()).block()) {
                        sink.error(new BookAppException(getGenreAlreadyExistsMessage(dto.getName())));
                    } else {
                        sink.next(genreMapper.toEntity(dto));
                    }
                });
        return genreRepository.save(genreToCreate).map(genreMapper::toDto);
    }

    @Transactional
    @Override
    public Mono<GenreDto> updateGenre(long id, Mono<GenreDto> genreDto) {
        return genreRepository.findById(id)
                .flatMap(s -> genreRepository
                        .save(genreDto.map(dto -> genreMapper.partialUpdate(dto, s)))
                        .map(genreMapper::toDto));
    }

    @Transactional
    @Override
    public Mono<GenreDto> deleteGenre(long id) {
        return genreRepository.findById(id)
                .flatMap(s -> genreRepository.delete(s)
                        .then(Mono.just(genreMapper.toDto(s))));
    }

}
