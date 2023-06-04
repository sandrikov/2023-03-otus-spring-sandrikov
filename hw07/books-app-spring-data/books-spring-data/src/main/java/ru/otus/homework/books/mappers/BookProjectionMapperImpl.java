package ru.otus.homework.books.mappers;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.otus.homework.books.domain.Book;
import ru.otus.homework.books.dto.BookProjection;

@Service
public class BookProjectionMapperImpl implements BookProjectionMapper {

    private final AuthorMapper authorMapper;

    private final GenreMapper genreMapper;

    public BookProjectionMapperImpl(AuthorMapper authorMapper, GenreMapper genreMapper) {
        this.authorMapper = authorMapper;
        this.genreMapper = genreMapper;
    }

    @Override
    public BookProjection toDto(Object[] data) {
        Assert.notEmpty(data, "Expected Book and numberOfComments");
        Assert.isTrue(data.length == 2, "Expected Book and numberOfComments");
        Assert.isInstanceOf(Book.class, data[0], "The 1st param expected Book");
        Assert.isInstanceOf(Long.class, data[1], "The 2nd param expected Long");
        Book book = (Book) data[0];
        Long count = (Long) data[1];
        return new BookProjection(book.getId(), book.getTitle(), authorMapper.toDto(book.getAuthor()),
                genreMapper.toDto(book.getGenre()), count);
    }

}
