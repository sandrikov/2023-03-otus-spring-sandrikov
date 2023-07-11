package ru.otus.homework.books.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;
import ru.otus.homework.books.domain.Genre;

public interface GenreRepository extends ReactiveCrudRepository<Genre, Long> {

    Mono<Boolean> existsByName(String name);

    Mono<Genre> save(Mono<Genre> person);

}
