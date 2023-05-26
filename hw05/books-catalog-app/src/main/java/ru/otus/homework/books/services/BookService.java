package ru.otus.homework.books.services;

import ru.otus.homework.books.model.Book;

import java.util.List;

public interface BookService {

    ServiceResponse<List<Book>> listBooks(Long authorId, Long genreId, String name);

    ServiceResponse<Book> getBook(Long id);

    ServiceResponse<Book> createBook(String name, Long authorId, Long genreId);

    ServiceResponse<Book> modifyBook(Long id, String name, Long authorId, Long genreId);

    ServiceResponse<Book> deleteBook(long id);

    ServiceResponse<Integer> deleteBooks(Long authorId, Long genreId);

}
