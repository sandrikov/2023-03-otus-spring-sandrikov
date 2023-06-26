package ru.otus.homework.books.services;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.homework.books.domain.Author;
import ru.otus.homework.books.domain.Book;
import ru.otus.homework.books.domain.Genre;
import ru.otus.homework.books.mappers.BookMapper;
import ru.otus.homework.books.repository.BookRepository;
import ru.otus.homework.books.rest.dto.BookDto;
import ru.otus.homework.books.rest.dto.BookProjection;
import ru.otus.homework.books.services.misc.EntityNotFoundException;
import ru.otus.homework.books.services.misc.Reply;
import ru.otus.homework.books.services.misc.ServiceUtils;

import java.util.List;
import java.util.Optional;

import static java.util.Optional.empty;
import static ru.otus.homework.books.services.misc.Reply.done;
import static ru.otus.homework.books.services.misc.Reply.error;
import static ru.otus.homework.books.services.misc.ServiceErrorMessages.BOOK_NOT_FOUND;
import static ru.otus.homework.books.services.misc.ServiceErrorMessages.getBookAlreadyExistsMessage;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private final AuthorService authorService;

    private final GenreService genreService;

    private final CommentService commentService;

    private final BookMapper bookMapper;

    @Transactional(readOnly = true)
    @Override
    public Reply<List<BookProjection>> listBooks(Long authorId, Long genreId, String title) {
        try {
            return done(findData(authorId, genreId, title));
        } catch (EntityNotFoundException e) {
            return error(e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Reply<BookDto> getBook(long id) {
        try {
            return done(bookMapper.toDto(findBook(id)));
        } catch (EntityNotFoundException e) {
            return error(e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Reply<BookProjection> getBookProjection(Long id) {
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
    public Reply<BookDto> createBook(String title, Long authorId, Long genreId) {
        Author author;
        Genre genre;
        try {
            author = authorService.findAuthor(authorId);
            genre = genreService.findGenre(genreId);
        } catch (EntityNotFoundException e) {
            return error(e.getMessage());
        }
        if (bookRepository.findByTitleAndAuthor(title, author).isPresent()) {
            return error(getBookAlreadyExistsMessage(author.getName(), title));
        }
        var book = new Book(title, author, genre);
        bookRepository.save(book);
        return done(bookMapper.toDto(book));
    }

    @Transactional
    @Override
    public Reply<BookProjection> modifyBook(Long id, String title, Long authorId, Long genreId) {
        try {
            val book = findBook(id);
            val author = authorService.findAuthor(authorId);
            val genre = genreService.findGenre(genreId);
            val existingBook = checkExistingBook(book, title, author);
            if (existingBook.isPresent()) {
                return error(existingBook.get());
            }
            partialUpdate(book, title, author, genre);
            bookRepository.saveAndFlush(book);
            long commentCount = commentService.countByBookId(book.getId());
            return done(bookMapper.toBookProjection(book, commentCount));
        } catch (EntityNotFoundException e) {
            return error(e.getMessage());
        }
    }

    @Override
    public Reply<BookDto> modifyBook(BookDto bookDto) {
        try {
            val book = findBook(bookDto.getId());
            val title = bookDto.getTitle();
            val author = bookDto.getAuthor() != null ?
                    authorService.findAuthor(bookDto.getAuthor().getId()) : null;
            val genre = bookDto.getGenre() != null ?
                    genreService.findGenre(bookDto.getGenre().getId()) : null;
            val existingBook = checkExistingBook(book, title, author);
            if (existingBook.isPresent()) {
                return error(existingBook.get());
            }
            partialUpdate(book, title, author, genre);
            bookRepository.saveAndFlush(book);
            return done(bookMapper.toDto(book));
        } catch (EntityNotFoundException e) {
            return error(e.getMessage());
        }
    }

    @Transactional
    @Override
    public Reply<BookProjection> deleteBook(long id) {
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
    public Reply<Integer> deleteBooks(Long authorId, Long genreId) {
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
    public Book findBook(Long bookId) {
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

    private List<BookProjection> findData(Long authorId, Long genreId, String title) {
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

    private void partialUpdate(Book book, String title, Author author, Genre genre) {
        if (title != null) {
            book.setTitle(title);
        }
        if (author != null) {
            book.setAuthor(author);
        }
        if (genre != null) {
            book.setGenre(genre);
        }
    }

    private Optional<String> checkExistingBook(Book bookToModify, String newTitle, Author newAuthor) {
        if (newTitle == null && newAuthor == null) {
            return empty();
        }
        val title = newTitle != null ? newTitle : bookToModify.getTitle();
        val author = newAuthor != null ? newAuthor : bookToModify.getAuthor();
        return bookRepository.findByTitleAndAuthor(title, author)
                .filter(b -> b.getId() != bookToModify.getId())
                .map(b -> getBookAlreadyExistsMessage(b.getAuthor().getName(), b.getTitle()));
    }

}
