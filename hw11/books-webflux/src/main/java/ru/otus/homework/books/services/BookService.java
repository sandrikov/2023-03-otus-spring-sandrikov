package ru.otus.homework.books.services;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.homework.books.rest.dto.BookDto;

public interface BookService {

    Flux<BookDto> listBooks();

    Mono<BookDto> getBook(long id);

    Mono<BookDto> createBook(Mono<BookDto> bookDto);

    Mono<BookDto> updateBook(Long id, Mono<BookDto> bookDto);

    Mono<BookDto> deleteBook(Long id);

}
