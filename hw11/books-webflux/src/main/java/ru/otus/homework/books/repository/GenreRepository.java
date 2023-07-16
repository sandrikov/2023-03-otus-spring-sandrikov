package ru.otus.homework.books.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import ru.otus.homework.books.domain.Genre;

public interface GenreRepository extends ReactiveCrudRepository<Genre, Long> {
}
