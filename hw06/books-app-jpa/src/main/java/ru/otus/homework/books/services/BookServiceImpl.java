package ru.otus.homework.books.services;

import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.homework.books.domain.Author;
import ru.otus.homework.books.domain.Book;
import ru.otus.homework.books.domain.Genre;
import ru.otus.homework.books.dto.BookDto;
import ru.otus.homework.books.dto.BookProjection;
import ru.otus.homework.books.mappers.BookMapper;
import ru.otus.homework.books.repository.AuthorRepository;
import ru.otus.homework.books.repository.BookRepository;
import ru.otus.homework.books.repository.CommentRepository;
import ru.otus.homework.books.repository.GenreRepository;
import ru.otus.homework.books.services.misc.EntityNotFoundException;
import ru.otus.homework.books.services.misc.ServiceUtils;

import java.util.List;

import static ru.otus.homework.books.services.AuthorServiceImpl.AUTHOR_NOT_FOUND;
import static ru.otus.homework.books.services.GenreServiceImpl.GENRE_NOT_FOUND;
import static ru.otus.homework.books.services.ServiceResponse.done;
import static ru.otus.homework.books.services.ServiceResponse.error;

@Service
public class BookServiceImpl implements BookService {

    public static final String BOOK_NOT_FOUND = "Book is not found: %s";

    public static final String BOOK_ALREADY_EXISTS = "Book already exists: %s '%s'";


    private final BookRepository bookRepository;

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final CommentRepository commentRepository;

    private final BookMapper bookMapper;



    public BookServiceImpl(BookRepository bookbookRepository, AuthorRepository authorRepository,
                           GenreRepository genreRepository, CommentRepository commentRepository,
                           BookMapper bookMapper) {
        this.bookRepository = bookbookRepository;
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
        this.commentRepository = commentRepository;
        this.bookMapper = bookMapper;
    }

    @Transactional(readOnly = true)
    @Override
    public ServiceResponse<List<BookProjection>> listBooks(Long authorId, Long genreId, String title) {
        try {
            return done(findData(authorId, genreId, title));
        } catch (EntityNotFoundException e) {
            return error(e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    @Override
    public ServiceResponse<BookDto> getBook(Long id) {
        try {
            return done(bookMapper.toDto(findBook(id)));
        } catch (EntityNotFoundException e) {
            return error(e.getMessage());
        }
    }

    @Transactional
    @Override
    public ServiceResponse<BookDto> createBook(String title, Long authorId, Long genreId) {
        Author author;
        Genre genre;
        try {
            author = findAuthor(authorId);
            genre = findGenre(genreId);
        } catch (EntityNotFoundException e) {
            return error(e.getMessage());
        }
        if (bookRepository.findByTitleAndAuthor(title, author).isPresent()) {
            return error(String.format(BOOK_ALREADY_EXISTS, author.getName(), title));
        }
        var book = new Book(title, author, genre);
        bookRepository.save(book);
        return done(bookMapper.toDto(book));
    }

    @Transactional
    @Override
    public ServiceResponse<BookDto> modifyBook(Long id, String title, Long authorId, Long genreId) {
        try {
            val author = findAuthor(authorId);
            val genre = findGenre(genreId);
            val book = findBook(id);
            if (title != null || author != null) {
                val existingBook = bookRepository.findByTitleAndAuthor(title != null ? title : book.getTitle(),
                        author != null ? author : book.getAuthor());
                if (existingBook.filter(b -> id != b.getId()).isPresent()) {
                    return error(String.format(BOOK_ALREADY_EXISTS, existingBook.get().getAuthor().getName(),
                            existingBook.get().getTitle()));
                }
            }
            partialUpdate(book, title, genreId, author, genre);
            bookRepository.save(book);
            return done(bookMapper.toDto(book));
        } catch (EntityNotFoundException e) {
            return error(e.getMessage());
        }
    }

    @Transactional
    @Override
    public ServiceResponse<BookDto> deleteBook(long id) {
        try {
            var book = findBook(id);
            bookRepository.delete(book);
            return done(bookMapper.toDto(book));
        } catch (EntityNotFoundException e) {
            return error(e.getMessage());
        }
    }

    @Transactional
    @Override
    public ServiceResponse<Integer> deleteBooks(Long authorId, Long genreId) {
        if (authorId == null && genreId == null) {
            commentRepository.deleteAllInBatch();
            return done(bookRepository.deleteAllInBatch());
        }
        try {
            val author = findAuthor(authorId);
            val genre = findGenre(genreId);
            val books = bookRepository.findAllByAuthorAndGenreAndTitle(author, genre, null);
            if (books.isEmpty()) {
                return done(0);
            }
            commentRepository.deleteAllByBooksInBatch(books);
            return done(bookRepository.deleteAllInBatch(books));
        } catch (EntityNotFoundException e) {
            return error(e.getMessage());
        }
    }

    private List<BookProjection> findData(Long authorId, Long genreId, String title) throws EntityNotFoundException {
        if (authorId == null && genreId == null && title == null) {
            return bookRepository.findAllWithStat();
        }
        final var author = findAuthor(authorId);
        final var genre = findGenre(genreId);
        return bookRepository.findAllWithStatByAuthorAndGenreAndTitle(author, genre, title);
    }

    private Genre findGenre(Long genreId) throws EntityNotFoundException {
        return ServiceUtils.findById(genreId, genreRepository::findById, GENRE_NOT_FOUND);
    }

    private Author findAuthor(Long authorId) throws EntityNotFoundException {
        return ServiceUtils.findById(authorId, authorRepository::findById, AUTHOR_NOT_FOUND);
    }

    private Book findBook(Long bookId) throws EntityNotFoundException {
        return ServiceUtils.findById(bookId, bookRepository::findById, BOOK_NOT_FOUND);
    }

    private void partialUpdate(Book book, String title, Long genreId, Author author, Genre genre) {
        if (title != null) {
            book.setTitle(title);
        }
        if (author != null) {
            book.setAuthor(author);
        }
        if (genreId != null) {
            book.setGenre(genre);
        }
    }

}
