package ru.otus.homework.books.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.homework.books.domain.Genre;

import java.util.Optional;

public interface GenreRepository extends JpaRepository<Genre, Long> {
    Optional<Genre> findById(long id);

    Optional<Genre> findByName(String name);

}
