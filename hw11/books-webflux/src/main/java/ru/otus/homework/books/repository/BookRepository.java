package ru.otus.homework.books.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;
import ru.otus.homework.books.domain.Author;
import ru.otus.homework.books.domain.Book;

public interface BookRepository extends ReactiveCrudRepository<Book, Long> {

    Mono<Book> save(Mono<Book> person);

    /**
     * Find by alternative key
     */
    Mono<Book> findByTitleAndAuthor(String title, Author author);

    Mono<Boolean> existsByAuthorId(long authorId);

    Mono<Boolean> existsByGenreId(long genreId);
}
