package ru.otus.homework.books.rest;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.homework.books.rest.dto.BookDto;
import ru.otus.homework.books.services.BookService;

import static org.springframework.http.ResponseEntity.notFound;
import static ru.otus.homework.books.rest.misc.ResponseUtil.toURI;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Log4j2
public class BookRestController {

    private final BookService bookService;

    @GetMapping("/books")
    public Flux<BookDto> getAllBooks() {
        log.debug("REST request to get all Books");
        return bookService.listBooks();
    }

    @GetMapping("/books/{id}")
    public Mono<ResponseEntity<BookDto>> getBook(@PathVariable Long id) {
        log.debug("REST request to get Book : {}", id);
        return bookService.getBook(id).map(ResponseEntity::ok)
                .switchIfEmpty(Mono.fromCallable(() -> notFound().build()));
    }

    @PostMapping("/books")
    public Mono<ResponseEntity<BookDto>> createBook(@RequestBody Mono<BookDto> bookDto) {
        log.debug("REST request to add Book");
        return bookService.createBook(bookDto)
                .map(dto -> ResponseEntity.created(toURI("/api/books/" + dto.getId())).body(dto));
    }

    @PutMapping("/books/{id}")
    public Mono<ResponseEntity<BookDto>> updateBook(@PathVariable(value = "id") final Long id,
                                              @RequestBody Mono<BookDto> bookDto) {
        log.debug("REST request to update Book : {}", bookDto);
        return bookService.updateBook(id, bookDto)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.fromCallable(() -> notFound().build()));
    }

    @DeleteMapping("/books/{id}")
    public Mono<ResponseEntity<Void>> deleteBook(@PathVariable Long id) {
        log.debug("REST request to delete Book : {}", id);
        return bookService.deleteBook(id)
                .then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)))
                .switchIfEmpty(Mono.fromCallable(() -> notFound().build()));
    }

}
