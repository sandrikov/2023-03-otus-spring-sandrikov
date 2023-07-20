package ru.otus.homework.books.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import ru.otus.homework.books.domain.Author;

public interface AuthorRepository extends ReactiveCrudRepository<Author, Long> {
}
