package ru.otus.homework.books.mappers;

import lombok.val;
import org.springframework.stereotype.Service;
import ru.otus.homework.books.domain.Genre;
import ru.otus.homework.books.rest.dto.GenreDto;

@Service
public class GenreMapperImpl implements GenreMapper {
    @Override
    public Genre toEntity(GenreDto dto) {
        if (dto == null) {
            return null;
        }
        return new Genre(dto.getId(), dto.getName());
    }

    @Override
    public GenreDto toDto(Genre genre) {
        if (genre == null) {
            return null;
        }
        return new GenreDto(genre.getId(), genre.getName());
    }

    @Override
    public Genre partialUpdate(GenreDto dto, Genre target) {
        val name = dto.getName() != null ? dto.getName() : target.getName();
        return new Genre(target.getId(), name);
    }
}
