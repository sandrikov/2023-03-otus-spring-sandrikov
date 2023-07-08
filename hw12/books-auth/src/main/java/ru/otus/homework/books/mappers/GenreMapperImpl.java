package ru.otus.homework.books.mappers;

import org.springframework.stereotype.Service;
import ru.otus.homework.books.domain.Genre;
import ru.otus.homework.books.dto.GenreDto;

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
}
