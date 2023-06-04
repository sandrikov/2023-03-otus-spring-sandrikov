package ru.otus.homework.books.repository;

import lombok.val;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Example;
import ru.otus.homework.books.domain.Author;
import ru.otus.homework.books.domain.Book;
import ru.otus.homework.books.domain.Genre;
import ru.otus.homework.books.dto.BookProjection;
import ru.otus.homework.books.mappers.BookProjectionMapper;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.data.domain.ExampleMatcher.matchingAll;

public class BookRepositoryCustomImpl implements BookRepositoryCustom {

    private final BookRepository bookRepository;

    private final BookProjectionMapper bookProjectionMapper;

    public BookRepositoryCustomImpl(@Lazy BookRepository bookRepository, BookProjectionMapper bookProjectionMapper) {
        this.bookRepository = bookRepository;
        this.bookProjectionMapper = bookProjectionMapper;
    }

    @Override
    public List<BookProjection> findAllBookProjections() {
        return bookRepository.findAllBookWithCommentCount().stream().map(bookProjectionMapper::toDto).toList();
    }

    @Override
    public List<BookProjection> findAllBookProjections(Author author, Genre genre, String title) {
        val example = createBookExampleWithIgnoreNullValues(author, genre, title);
        val books = bookRepository.findAll(example);
        val commentCountOfBooks = bookRepository.getCountGroupByBookIdFindByBookIn(books)
                .collect(Collectors.toMap(t -> (Long)t[0], t -> (Long)t[1]));
        return books.stream()
                .map(book -> bookProjectionMapper.toDto(book,
                        commentCountOfBooks.getOrDefault(book.getId(), 0L)))
                .toList();
    }

    @Override
    public long countByAuthorAndGenreAndTitle(Author author, Genre genre, String title) {
        if (author == null && genre == null && title == null) {
            return bookRepository.count();
        }
        val example = createBookExampleWithIgnoreNullValues(author, genre, title);
        return bookRepository.count(example);
    }

    private Example<Book> createBookExampleWithIgnoreNullValues(Author author, Genre genre, String title) {
        val probe = new Book(title, author, genre);
        return Example.of(probe, matchingAll().withIgnoreNullValues());
    }

}
