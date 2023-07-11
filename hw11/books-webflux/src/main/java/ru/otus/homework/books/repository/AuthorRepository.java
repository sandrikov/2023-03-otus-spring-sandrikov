package ru.otus.homework.books.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;
import ru.otus.homework.books.domain.Author;

public interface AuthorRepository extends ReactiveCrudRepository<Author, Long> {

    Mono<Boolean> existsByName(String name);

    Mono<Author> save(Mono<Author> person);
}
