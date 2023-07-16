package ru.otus.homework.books.mappers;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import ru.otus.homework.books.domain.Book;
import ru.otus.homework.books.rest.dto.BookDto;


@RequiredArgsConstructor
@Service
public class BookMapperImpl implements BookMapper {

    private final GenreMapper genreMapper;

    private final AuthorMapper authorMapper;

    @Override
    public Book toEntity(BookDto dto) {
        return new Book(dto.getId(), dto.getTitle(),
                authorMapper.toEntity(dto.getAuthor()),
                genreMapper.toEntity(dto.getGenre()));
    }

    @Override
    public BookDto toDto(Book book) {
        return new BookDto(book.getId(), book.getTitle(),
                authorMapper.toDto(book.getAuthor()),
                genreMapper.toDto(book.getGenre()));
    }

    @Override
    public Book partialUpdate(BookDto dto, Book target) {
        if (dto == null) {
            return target;
        }
        val title = dto.getTitle() != null ? dto.getTitle() : target.getTitle();
        val author = dto.getAuthor() != null ? authorMapper.toEntity(dto.getAuthor()) : target.getAuthor();
        val genre = dto.getGenre() != null ? genreMapper.toEntity(dto.getGenre()) : target.getGenre();
        return new Book(target.getId(), title, author, genre);
    }
}
