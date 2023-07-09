package ru.otus.homework.books.mappers;

import ru.otus.homework.books.rest.dto.AuthorDto;
import ru.otus.homework.books.domain.Author;

public interface AuthorMapper {
    Author toEntity(AuthorDto authorDto);

    AuthorDto toDto(Author author);

}
