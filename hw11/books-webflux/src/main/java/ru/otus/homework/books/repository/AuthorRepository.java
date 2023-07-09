package ru.otus.homework.books.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;
import ru.otus.homework.books.domain.Author;

import java.util.Optional;

public interface AuthorRepository extends ReactiveCrudRepository<Author, Long> {

    Mono<Author> findByName(String name);

    Mono<Author> save(Mono<Author> person);
}
