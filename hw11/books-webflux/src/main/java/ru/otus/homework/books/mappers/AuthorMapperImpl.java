package ru.otus.homework.books.mappers;

import lombok.val;
import org.springframework.stereotype.Component;
import ru.otus.homework.books.domain.Author;
import ru.otus.homework.books.rest.dto.AuthorDto;

@Component
public class AuthorMapperImpl implements AuthorMapper {
    @Override
    public Author toEntity(AuthorDto dto) {
        if (dto == null) {
            return null;
        }
        return new Author(dto.getId(), dto.getName());
    }

    @Override
    public AuthorDto toDto(Author author) {
        if (author == null) {
            return null;
        }
        return new AuthorDto(author.getId(), author.getName());
    }

    @Override
    public Author partialUpdate(AuthorDto authorDto, Author target) {
        val name = authorDto.getName() != null ? authorDto.getName() : target.getName();
        return new Author(target.getId(), name);
    }

}
