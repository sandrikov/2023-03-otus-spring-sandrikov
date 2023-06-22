package ru.otus.homework.books.repository;

import org.springframework.context.annotation.Lazy;
import ru.otus.homework.books.mappers.BookMapper;
import ru.otus.homework.books.rest.dto.BookProjection;

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

}
