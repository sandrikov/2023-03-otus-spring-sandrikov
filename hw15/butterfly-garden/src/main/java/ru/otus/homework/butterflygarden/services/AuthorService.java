package ru.otus.homework.butterflygarden .services;

import ru.otus.homework.butterflygarden .domain.Author;
import ru.otus.homework.butterflygarden .dto.AuthorDto;
import ru.otus.homework.butterflygarden .services.misc.EntityNotFoundException;

import java.util.List;


public interface AuthorService {
    ServiceResponse<List<AuthorDto>> listAuthors();

    ServiceResponse<AuthorDto> getAuthor(long id);

    ServiceResponse<AuthorDto> findAuthor(String name);

    ServiceResponse<AuthorDto> createAuthor(String name);

    ServiceResponse<AuthorDto> renameAuthor(long id, String name);

    ServiceResponse<AuthorDto> deleteAuthor(long id);

    Author findAuthor(Long genreId) throws EntityNotFoundException;
}
