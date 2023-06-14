package ru.otus.homework.books.services;

import ru.otus.homework.books.domain.Book;
import ru.otus.homework.books.dto.BookDto;
import ru.otus.homework.books.dto.BookProjection;
import ru.otus.homework.books.services.misc.EntityNotFoundException;

import java.util.List;

public interface BookService {

    ServiceResponse<List<BookProjection>> listBooks(Long authorId, Long genreId, String name);

    ServiceResponse<BookDto> getBook(long id);

    ServiceResponse<BookProjection> getBookProjection(Long id);

    ServiceResponse<BookProjection> createBook(String name, Long authorId, Long genreId);

    ServiceResponse<BookProjection> modifyBook(Long id, String name, Long authorId, Long genreId);

    ServiceResponse<BookProjection> deleteBook(long id);

    ServiceResponse<Integer> deleteBooks(Long authorId, Long genreId);

    Book findBook(Long bookId) throws EntityNotFoundException;

    boolean existsByAuthorId(long authorId);

    boolean existsByGenreId(long genreId);
}
