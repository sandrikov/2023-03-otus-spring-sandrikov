package ru.otus.homework.books.mappers;

import ru.otus.homework.books.domain.Author;
import ru.otus.homework.books.rest.dto.AuthorDto;

public interface AuthorMapper {
    Author toEntity(AuthorDto authorDto);

    AuthorDto toDto(Author author);

}
