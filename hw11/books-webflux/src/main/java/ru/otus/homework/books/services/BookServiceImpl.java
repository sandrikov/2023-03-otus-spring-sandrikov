package ru.otus.homework.books.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.homework.books.mappers.BookMapper;
import ru.otus.homework.books.repository.BookRepository;
import ru.otus.homework.books.rest.dto.BookDto;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private final BookMapper bookMapper;

    @Transactional(readOnly = true)
    @Override
    public Flux<BookDto> listBooks() {
        return bookRepository.findAll().map(bookMapper::toDto);
    }

    @Transactional(readOnly = true)
    @Override
    public Mono<BookDto> getBook(long id) {
        return bookRepository.findById(id).map(bookMapper::toDto);
    }

    @Transactional
    @Override
    public Mono<BookDto> createBook(Mono<BookDto> bookDto) {
        return bookRepository.save(bookDto.map(bookMapper::toEntity)).map(bookMapper::toDto);
    }

    @Transactional
    @Override
    public Mono<BookDto> updateBook(Long id, Mono<BookDto> bookDto) {
        return bookRepository.findById(id)
                .flatMap(s -> bookRepository
                        .save(bookDto.map(dto -> bookMapper.partialUpdate(dto, s)))
                        .map(bookMapper::toDto));
    }

    @Transactional
    @Override
    public Mono<BookDto> deleteBook(Long id) {
        return bookRepository.findById(id)
                .flatMap(s -> bookRepository.delete(s)
                        .then(Mono.just(bookMapper.toDto(s))));
    }

}
