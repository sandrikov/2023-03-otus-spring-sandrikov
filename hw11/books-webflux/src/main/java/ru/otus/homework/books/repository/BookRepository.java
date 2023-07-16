package ru.otus.homework.books.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import ru.otus.homework.books.domain.Book;

public interface BookRepository extends ReactiveCrudRepository<Book, Long> {

}
