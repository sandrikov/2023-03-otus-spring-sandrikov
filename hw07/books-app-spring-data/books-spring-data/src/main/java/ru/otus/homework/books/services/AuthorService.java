package ru.otus.homework.books.services;

import ru.otus.homework.books.dto.AuthorDto;

import java.util.List;


public interface AuthorService {
    ServiceResponse<List<AuthorDto>> listAuthors();

    ServiceResponse<AuthorDto> getAuthor(long id);

    ServiceResponse<AuthorDto> findAuthor(String name);

    ServiceResponse<AuthorDto> createAuthor(String name);

    ServiceResponse<AuthorDto> renameAuthor(long id, String name);

    ServiceResponse<AuthorDto> deleteAuthor(long id);
}
