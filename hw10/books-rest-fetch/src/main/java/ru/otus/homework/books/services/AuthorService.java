package ru.otus.homework.books.services;

import ru.otus.homework.books.domain.Author;
import ru.otus.homework.books.rest.dto.AuthorDto;
import ru.otus.homework.books.services.misc.Reply;

import java.util.List;


public interface AuthorService {

    Reply<List<AuthorDto>> listAuthors();

    Reply<AuthorDto> getAuthor(long id);

    Reply<AuthorDto> findAuthor(String name);

    Reply<AuthorDto> createAuthor(String name);

    Reply<AuthorDto> renameAuthor(long id, String name);

    Reply<AuthorDto> deleteAuthor(long id);

    Author findAuthor(Long genreId);

}
