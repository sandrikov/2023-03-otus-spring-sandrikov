package ru.otus.homework.books.mappers;

import ru.otus.homework.books.domain.Book;
import ru.otus.homework.books.rest.dto.BookDto;

public interface BookMapper {
    Book toEntity(BookDto bookDto);

    BookDto toDto(Book book);

    Book partialUpdate(BookDto bookDto, Book target);

}
