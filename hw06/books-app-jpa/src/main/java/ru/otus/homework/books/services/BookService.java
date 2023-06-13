package ru.otus.homework.books.services;

import ru.otus.homework.books.dto.BookDto;
import ru.otus.homework.books.dto.BookProjection;

import java.util.List;

public interface BookService {

    ServiceResponse<List<BookProjection>> listBooks(Long authorId, Long genreId, String name);

    ServiceResponse<BookDto> getBook(Long id);

    ServiceResponse<BookProjection> getBookProjection(Long id);

    ServiceResponse<BookProjection> createBook(String name, Long authorId, Long genreId);

    ServiceResponse<BookProjection> modifyBook(Long id, String name, Long authorId, Long genreId);

    ServiceResponse<BookProjection> deleteBook(long id);

    ServiceResponse<Integer> deleteBooks(Long authorId, Long genreId);

}
