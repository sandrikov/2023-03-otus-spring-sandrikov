package ru.otus.homework.books.services;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.homework.books.domain.Author;
import ru.otus.homework.books.domain.Book;
import ru.otus.homework.books.domain.Genre;
import ru.otus.homework.books.dto.BookDto;
import ru.otus.homework.books.dto.BookProjection;
import ru.otus.homework.books.mappers.BookMapper;
import ru.otus.homework.books.repository.BookRepository;
import ru.otus.homework.books.services.misc.EntityNotFoundException;
import ru.otus.homework.books.services.misc.ServiceUtils;

import java.util.List;

import static ru.otus.homework.books.services.ServiceResponse.done;
import static ru.otus.homework.books.services.ServiceResponse.error;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    public static final String BOOK_NOT_FOUND = "Book is not found: %s";

    public static final String BOOK_ALREADY_EXISTS = "Book already exists: %s '%s'";

    private final BookRepository bookRepository;

    private final AuthorService authorService;

    private final GenreService genreService;

    private final CommentService commentService;

    private final BookMapper bookMapper;

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
    public ServiceResponse<BookDto> getBook(long id) {
        try {
            return done(bookMapper.toDto(findBook(id)));
        } catch (EntityNotFoundException e) {
            return error(e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    @Override
    public ServiceResponse<BookProjection> getBookProjection(Long id) {
        try {
            val book = findBook(id);
            long commentCount = commentService.countByBookId(book.getId());
            return done(bookMapper.toBookProjection(book, commentCount));
        } catch (EntityNotFoundException e) {
            return error(e.getMessage());
        }
    }

    @Transactional
    @Override
    public ServiceResponse<BookProjection> createBook(String title, Long authorId, Long genreId) {
        Author author;
        Genre genre;
        try {
            author = authorService.findAuthor(authorId);
            genre = genreService.findGenre(genreId);
        } catch (EntityNotFoundException e) {
            return error(e.getMessage());
        }
        if (bookRepository.findByTitleAndAuthor(title, author).isPresent()) {
            return error(String.format(BOOK_ALREADY_EXISTS, author.getName(), title));
        }
        var book = new Book(title, author, genre);
        bookRepository.save(book);
        return done(bookMapper.toBookProjection(book, 0L));
    }

    @Transactional
    @Override
    public ServiceResponse<BookProjection> modifyBook(Long id, String title, Long authorId, Long genreId) {
        try {
            val author = authorService.findAuthor(authorId);
            val genre = genreService.findGenre(genreId);
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
            long commentCount = commentService.countByBookId(book.getId());
            return done(bookMapper.toBookProjection(book, commentCount));
        } catch (EntityNotFoundException e) {
            return error(e.getMessage());
        }
    }

    @Transactional
    @Override
    public ServiceResponse<BookProjection> deleteBook(long id) {
        try {
            var book = findBook(id);
            bookRepository.delete(book);
            return done(bookMapper.toBookProjection(book, 0L));
        } catch (EntityNotFoundException e) {
            return error(e.getMessage());
        }
    }

    @Transactional
    @Override
    public ServiceResponse<Integer> deleteBooks(Long authorId, Long genreId) {
        try {
            val author = authorService.findAuthor(authorId);
            val genre = genreService.findGenre(genreId);
            val books = bookRepository.findAll(bookRepository.createExample(author, genre, null));
            if (books.isEmpty()) {
                return done(0);
            }
            val bookIds = books.stream().map(Book::getId).toList();
            commentService.deleteComments(bookIds);
            bookRepository.deleteAllInBatch(books);
            return done(books.size());
        } catch (EntityNotFoundException e) {
            return error(e.getMessage());
        }
    }

    @Override
    public Book findBook(Long bookId) throws EntityNotFoundException {
        return ServiceUtils.findById(bookId, bookRepository::findById, BOOK_NOT_FOUND);
    }

    @Override
    public boolean existsByAuthorId(long authorId) {
        return bookRepository.existsByAuthorId(authorId);
    }

    @Override
    public boolean existsByGenreId(long genreId) {
        return bookRepository.existsByGenreId(genreId);
    }

    private List<BookProjection> findData(Long authorId, Long genreId, String title) throws EntityNotFoundException {
        if (authorId == null && genreId == null && title == null) {
            return bookRepository.findAllBookProjections();
        }
        val author = authorService.findAuthor(authorId);
        val genre = genreService.findGenre(genreId);
        val books = bookRepository.findAll(bookRepository.createExample(author, genre, title));
        val bookIds = books.stream().map(Book::getId).toList();
        val commentCountOfBooks = commentService.countGroupByBookId(bookIds);
        return books.stream()
                .map(book -> bookMapper.toBookProjection(book,
                        commentCountOfBooks.getOrDefault(book.getId(), 0L)))
                .toList();
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
