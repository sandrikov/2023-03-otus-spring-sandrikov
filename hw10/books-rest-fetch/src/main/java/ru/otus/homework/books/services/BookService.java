package ru.otus.homework.books.services;

import ru.otus.homework.books.domain.Book;
import ru.otus.homework.books.rest.dto.BookDto;
import ru.otus.homework.books.rest.dto.BookProjection;
import ru.otus.homework.books.services.misc.Reply;

import java.util.List;

public interface BookService {

    Reply<List<BookProjection>> listBooks(Long authorId, Long genreId, String name);

    Reply<BookDto> getBook(long id);

    Reply<BookProjection> getBookProjection(Long id);

    Reply<BookDto> createBook(String name, Long authorId, Long genreId);

    Reply<BookProjection> modifyBook(Long id, String name, Long authorId, Long genreId);

    Reply<BookDto> modifyBook(BookDto bookDto);

    Reply<BookProjection> deleteBook(long id);

    Reply<Integer> deleteBooks(Long authorId, Long genreId);

    Book findBook(Long bookId);

    boolean existsByAuthorId(long authorId);

    boolean existsByGenreId(long genreId);

}
