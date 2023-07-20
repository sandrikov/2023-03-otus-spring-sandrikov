package ru.otus.homework.books.services;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.homework.books.domain.Author;
import ru.otus.homework.books.rest.dto.AuthorDto;


public interface AuthorService {

    Flux<AuthorDto> listAuthors();

    Mono<AuthorDto> getAuthor(Long id);

    Mono<AuthorDto> createAuthor(Mono<AuthorDto> authorDto);

    Mono<AuthorDto> updateAuthor(Long id, Mono<AuthorDto> authorDto);

    Mono<AuthorDto> deleteAuthor(Long id);

    Mono<Author> findAuthor(Long id);
}
