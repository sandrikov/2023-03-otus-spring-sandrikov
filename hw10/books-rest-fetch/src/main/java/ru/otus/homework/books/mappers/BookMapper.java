package ru.otus.homework.books.mappers;

import org.springframework.util.Assert;
import ru.otus.homework.books.domain.Book;
import ru.otus.homework.books.rest.dto.BookDto;
import ru.otus.homework.books.rest.dto.BookProjection;

public interface BookMapper {
    Book toEntity(BookDto bookDto);

    BookDto toDto(Book book);

    void partialUpdate(BookDto bookDto, Book target);

    BookProjection toBookProjection(Book book, long numberOfComments);

    default BookProjection toBookProjection(Object... data) {
        Assert.notEmpty(data, "Expected Book and numberOfComments");
        Assert.isTrue(data.length == 2, "Expected Book and numberOfComments");
        Assert.isInstanceOf(Book.class, data[0], "The 1st param expected Book");
        Assert.isInstanceOf(Long.class, data[1], "The 2nd param expected Long");
        return toBookProjection((Book) data[0], (Long) data[1]);
    }
}
