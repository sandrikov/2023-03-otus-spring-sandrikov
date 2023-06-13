package ru.otus.homework.books.repository;

import lombok.val;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import ru.otus.homework.books.domain.Author;
import ru.otus.homework.books.domain.Book;
import ru.otus.homework.books.domain.Genre;
import ru.otus.homework.books.dto.BookProjection;
import ru.otus.homework.books.mappers.BookMapper;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.data.domain.ExampleMatcher.matchingAll;

public class BookRepositoryCustomImpl implements BookRepositoryCustom {

    private static final ExampleMatcher EXAMPLE_MATCHER = matchingAll()
            .withIgnoreNullValues().withIgnorePaths("id", "author.name", "genre.name");

    private final BookRepository bookRepository;

    private final BookMapper bookMapper;

    public BookRepositoryCustomImpl(@Lazy BookRepository bookRepository, BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }

    @Override
    public List<BookProjection> findAllBookProjections() {
        return bookRepository.findAllBookWithCommentCount().stream().map(bookMapper::toBookProjection).toList();
    }

    @Override
    public List<BookProjection> findAllBookProjections(Author author, Genre genre, String title) {
        val example = createBookExampleWithIgnoreNullValues(author, genre, title);
        val books = bookRepository.findAll(example);
        val commentCountOfBooks = bookRepository.getCountGroupByBookIdFindByBookIn(books)
                .collect(Collectors.toMap(t -> (Long)t[0], t -> (Long)t[1]));
        return books.stream()
                .map(book -> bookMapper.toBookProjection(book,
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
        return Example.of(probe, EXAMPLE_MATCHER);
    }

}
