package ru.otus.homework.books.repository;

import lombok.val;
import org.springframework.context.annotation.Lazy;
import ru.otus.homework.books.domain.Author;
import ru.otus.homework.books.domain.Genre;
import ru.otus.homework.books.dto.BookProjection;
import ru.otus.homework.books.mappers.BookMapper;

import java.util.List;

public class BookRepositoryCustomImpl implements BookRepositoryCustom {

    private final BookRepository bookRepository;

    private final BookMapper bookMapper;

    public BookRepositoryCustomImpl(@Lazy BookRepository bookRepository, BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }

    @Override
    public List<BookProjection> findAllBookProjections() {
        return bookRepository.findAllBookWithCommentCount().stream()
                .map(bookMapper::toBookProjection).toList();
    }

    @Override
    public long countByAuthorAndGenreAndTitle(Author author, Genre genre, String title) {
        if (author == null && genre == null && title == null) {
            return bookRepository.count();
        }
        val example = createExample(author, genre, title);
        return bookRepository.count(example);
    }

}
