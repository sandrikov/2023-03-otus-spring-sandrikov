package ru.otus.homework.books.services;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.homework.books.rest.dto.AuthorDto;
import ru.otus.homework.books.domain.Author;
import ru.otus.homework.books.services.misc.Reply;


public interface AuthorService {

    Flux<AuthorDto> listAuthors();

    Mono<AuthorDto> getAuthor(Long id);

    Mono<AuthorDto> findAuthor(String name);

    Mono<AuthorDto> createAuthor(Mono<AuthorDto> authorDto);

    Mono<AuthorDto> updateAuthor(Long id, Mono<AuthorDto> authorDto);

    Reply<AuthorDto> deleteAuthor(Long id);

    Author findAuthor(Long genreId);

}
