package ru.otus.homework.books.services;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.otus.homework.books.dao.AuthorRepository;
import ru.otus.homework.books.dao.BookRepository;
import ru.otus.homework.books.dao.GenreRepository;
import ru.otus.homework.books.model.Author;
import ru.otus.homework.books.model.Book;
import ru.otus.homework.books.model.Genre;

import java.util.List;

import static ru.otus.homework.books.services.AuthorServiceImpl.AUTHOR_NOT_FOUND;
import static ru.otus.homework.books.services.GenreServiceImpl.GENRE_NOT_FOUND;
import static ru.otus.homework.books.services.ServiceResponse.done;
import static ru.otus.homework.books.services.ServiceResponse.error;

@Service
public class BookServiceImpl implements BookService {

    public static final String BOOK_NOT_FOUND = "Book is not found: %s";

    public static final String BOOK_ALREADY_EXISTS = "Book already exists: %s '%s'";

    private final BookRepository bookDao;

    private final AuthorRepository authorDao;

    private final GenreRepository genreDao;

    public BookServiceImpl(BookRepository bookDao, AuthorRepository authorDao, GenreRepository genreDao) {
        this.bookDao = bookDao;
        this.authorDao = authorDao;
        this.genreDao = genreDao;
    }

    @Override
    public ServiceResponse<List<Book>> listBooks(Long authorId, Long genreId, String name) {
        try {
            return done(findData(authorId, genreId, name));
        } catch (EntityNotFoundException e) {
            return error(e.getMessage());
        }
    }

    @Override
    public ServiceResponse<Book> getBook(Long id) {
        try {
            return done(findBook(id));
        } catch (EntityNotFoundException e) {
            return error(e.getMessage());
        }
    }

    @Override
    public ServiceResponse<Book> createBook(String name, Long authorId, Long genreId) {
        Author author;
        Genre genre;
        try {
            author = findAuthor(authorId);
            genre = findGenre(genreId);
        } catch (EntityNotFoundException e) {
            return error(e.getMessage());
        }
        var book = new Book(name, author, genre);
        try {
            bookDao.save(book);
            return done(book);
        } catch (DuplicateKeyException e) {
            return error(String.format(BOOK_ALREADY_EXISTS, book.getAuthor().getName(), book.getName()));
        }
    }

    @Override
    public ServiceResponse<Book> modifyBook(Long id, String name, Long authorId, Long genreId) {
        Book book;
        try {
            book = findBook(id);
            if (name != null) {
                book.setName(name);
            }
            if (authorId != null) {
                book.setAuthor(findAuthor(authorId));
            }
            if (genreId != null) {
                book.setGenre(findGenre(genreId));
            }
        } catch (EntityNotFoundException e) {
            return error(e.getMessage());
        }
        try {
            bookDao.save(book);
            return done(book);
        } catch (DuplicateKeyException e) {
            return error(String.format(BOOK_ALREADY_EXISTS, book.getAuthor().getName(), book.getName()));
        }
    }

    @Override
    public ServiceResponse<Book> deleteBook(long id) {
        try {
            var book = findBook(id);
            bookDao.delete(book);
            return done(book);
        } catch (EntityNotFoundException e) {
            return error(e.getMessage());
        }
    }

    @Override
    public ServiceResponse<Integer> deleteBooks(Long authorId, Long genreId) {
        try {
            var author = findAuthor(authorId);
            var genre = findGenre(genreId);
            if (author != null) {
                if (genre != null) {
                    return done(bookDao.deleteAllByAuthorAndGenre(author, genre));
                }
                return done(bookDao.deleteAllByAuthor(author));
            }
            if (genre != null) {
                return done(bookDao.deleteAllByGenre(genre));
            }
            return done(bookDao.deleteAll());
        } catch (EntityNotFoundException e) {
            return error(e.getMessage());
        }
    }

    private List<Book> findData(Long authorId, Long genreId, String name) throws EntityNotFoundException {
        final var author = findAuthor(authorId);
        final var genre = findGenre(genreId);
        if (name != null) {
            if (author == null && genre == null) {
                return bookDao.findByName(name);
            }
            return bookDao.findByName(name).stream()
                    .filter(t -> (genre == null || genre.equals(t.getGenre())) &&
                            (author == null || author.equals(t.getAuthor()))).toList();
        }
        if (author != null && genre != null) {
            return bookDao.findByAuthor(author).stream()
                    .filter(t -> genre.equals(t.getGenre())).toList();
        }
        if (author != null) {
            return bookDao.findByAuthor(author);
        }
        if (genre != null) {
            return bookDao.findByGenre(genre);
        }
        return bookDao.findAll();
    }

    private Genre findGenre(Long genreId) throws EntityNotFoundException {
        try {
            return genreId != null ? genreDao.findById(genreId) : null;
        } catch (EmptyResultDataAccessException e) {
            var message = String.format(GENRE_NOT_FOUND, "id=" + genreId);
            throw new EntityNotFoundException(message, e);
        }
    }

    private Author findAuthor(Long authorId) throws EntityNotFoundException {
        try {
            return authorId != null ? authorDao.findById(authorId) : null;
        } catch (EmptyResultDataAccessException e) {
            var message = String.format(AUTHOR_NOT_FOUND, "id=" + authorId);
            throw new EntityNotFoundException(message, e);
        }
    }

    private Book findBook(Long bookId) throws EntityNotFoundException {
        try {
            return bookDao.findById(bookId);
        } catch (EmptyResultDataAccessException e) {
            var message = String.format(BOOK_NOT_FOUND, "id=" + bookId);
            throw new EntityNotFoundException(message, e);
        }
    }

    private static class EntityNotFoundException extends Exception {
        public EntityNotFoundException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
