package ru.otus.homework.books.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.homework.books.domain.Book;
import ru.otus.homework.books.mappers.BookMapper;
import ru.otus.homework.books.repository.BookRepository;
import ru.otus.homework.books.rest.dto.BookDto;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private final BookMapper bookMapper;

    private final AuthorService authorService;

    private final GenreService genreService;

    @Override
    public Flux<BookDto> listBooks() {
        return bookRepository.findAll()
                .flatMap(this::loadRelations).map(bookMapper::toDto);
    }

    @Override
    public Mono<BookDto> getBook(long id) {
        return bookRepository.findById(id)
                .flatMap(this::loadRelations).map(bookMapper::toDto);
    }

    @Override
    public Mono<BookDto> createBook(Mono<BookDto> bookDto) {
        return bookDto.map(bookMapper::toEntity)
                .flatMap(book -> bookRepository.save(book)
                        .flatMap(this::loadRelations).map(bookMapper::toDto));
    }

    @Override
    public Mono<BookDto> updateBook(Long id, Mono<BookDto> bookDto) {
        return bookRepository.findById(id)
                .zipWith(bookDto)
                .map(t -> bookMapper.partialUpdate(t.getT2(), t.getT1()))
                .flatMap(book -> bookRepository.save(book)
                        .flatMap(this::loadRelations).map(bookMapper::toDto));
    }

    @Override
    public Mono<BookDto> deleteBook(Long id) {
        return bookRepository.findById(id)
                .flatMap(s -> bookRepository.delete(s)
                        .then(Mono.just(bookMapper.toDto(s))));
    }

    private Mono<Book> loadRelations(final Book book) {
        return Mono.just(book)
                .zipWith(authorService.findAuthor(book.getAuthorId()), Book::setAuthor)
                .zipWith(genreService.findGenre(book.getGenreId()), Book::setGenre);
    }
}
