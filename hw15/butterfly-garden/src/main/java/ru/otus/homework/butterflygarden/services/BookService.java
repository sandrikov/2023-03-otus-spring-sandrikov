package ru.otus.homework.butterflygarden .services;

import ru.otus.homework.butterflygarden .domain.Book;
import ru.otus.homework.butterflygarden .dto.BookDto;
import ru.otus.homework.butterflygarden .dto.BookProjection;
import ru.otus.homework.butterflygarden .services.misc.EntityNotFoundException;

import java.util.List;

public interface BookService {

    List<BookProjection> listBooks();

   ServiceResponse<List<BookProjection>> listBooks(Long authorId, Long genreId, String name);

    ServiceResponse<BookDto> getBook(long id);

    ServiceResponse<BookProjection> getBookProjection(Long id);

    ServiceResponse<BookProjection> createBook(String name, Long authorId, Long genreId, Integer ageLimit);

    ServiceResponse<BookProjection> modifyBook(Long id, String name, Long authorId, Long genreId, Integer ageLimit);

    ServiceResponse<BookDto> modifyBook(BookDto bookDto);

    ServiceResponse<BookProjection> deleteBook(long id);

    ServiceResponse<Integer> deleteBooks(Long authorId, Long genreId);

    Book findBook(Long bookId) throws EntityNotFoundException;

    boolean existsByAuthorId(long authorId);

    boolean existsByGenreId(long genreId);
}
