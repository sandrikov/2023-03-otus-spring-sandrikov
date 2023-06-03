package ru.otus.homework.books.mappers;

import ru.otus.homework.books.dto.BookProjection;

public interface BookProjectionMapper {

    BookProjection toDto(Object[] data);

}
