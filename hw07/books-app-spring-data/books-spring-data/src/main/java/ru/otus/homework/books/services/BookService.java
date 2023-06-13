package ru.otus.homework.books.services;

import ru.otus.homework.books.dto.BookDto;
import ru.otus.homework.books.dto.BookProjection;

import java.util.List;

public interface BookService {

    ServiceResponse<List<BookProjection>> listBooks(Long authorId, Long genreId, String name);

    ServiceResponse<BookDto> getBook(Long id);

    ServiceResponse<BookDto> createBook(String name, Long authorId, Long genreId);

    ServiceResponse<BookDto> modifyBook(Long id, String name, Long authorId, Long genreId);

    ServiceResponse<BookDto> deleteBook(long id);

    ServiceResponse<Integer> deleteBooks(Long authorId, Long genreId);

}