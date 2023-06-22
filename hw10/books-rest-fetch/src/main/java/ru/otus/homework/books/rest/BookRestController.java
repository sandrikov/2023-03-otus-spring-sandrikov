package ru.otus.homework.books.rest;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import ru.otus.homework.books.rest.misc.BookAppException;
import ru.otus.homework.books.rest.dto.BookDto;
import ru.otus.homework.books.rest.dto.BookProjection;
import ru.otus.homework.books.rest.misc.ResponseUtil;
import ru.otus.homework.books.services.BookService;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;

import static ru.otus.homework.books.rest.misc.RestErrorMessages.getInvalidIdMessage;
import static ru.otus.homework.books.rest.misc.RestErrorMessages.getNewEntityHaveIdMessage;
import static ru.otus.homework.books.services.misc.ServiceErrorMessages.getBookNotFoundMessage;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Log4j2
public class BookRestController {

    private final BookService bookService;

    @GetMapping("/books")
    public List<BookProjection> getAllBooks() {
        log.debug("REST request to get all Books");
        var response = bookService.listBooks(null, null, null);
        return response.getData();
    }

    @PostMapping("/books")
    public ResponseEntity<BookDto> createBook(@Valid @RequestBody BookDto book) throws URISyntaxException {
        log.debug("REST request to add Book : {}", book);
        if (book.getId() != 0) {
            throw new BookAppException(getNewEntityHaveIdMessage("book"));
        }
        val result = bookService.createBook(book.getTitle(), book.getAuthor().getId(), book.getGenre().getId())
                .orElseThrow(BookAppException::new);
        return ResponseEntity
                .created(new URI("/api/books/" + result.getId()))
                .body(result);
    }

    @PutMapping("/books/{id}")
    public ResponseEntity<BookDto> updateBook(@PathVariable(value = "id") final Long id,
                                              @Valid @RequestBody BookDto book) {
        log.debug("REST request to update Book : {}, {}", id, book);
        if (!Objects.equals(id, book.getId())) {
            throw new BookAppException(getInvalidIdMessage(id, book.getId()));
        }
        val result = bookService.modifyBook(book).orElseThrow(BookAppException::new);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/books/{id}")
    public ResponseEntity<BookDto> getBook(@PathVariable Long id) {
        log.debug("REST request to get Book : {}", id);
        return ResponseUtil.wrapOrNotFound(bookService.getBook(id));
    }

    @DeleteMapping("/books/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        log.debug("REST request to delete Book : {}", id);
        bookService.deleteBook(id)
                .orElseThrow(e -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(value = "/books/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BookDto> partialUpdateBook(
            @PathVariable(value = "id") final Long id,
            @NotNull @RequestBody BookDto book) {
        log.debug("REST request to partial update Book partially : {}, {}", id, book);
        if (!Objects.equals(id, book.getId())) {
            throw new BookAppException(getInvalidIdMessage(id, book.getId()));
        }
        val patchedBook = bookService.modifyBook(book)
                .orElseThrow(e -> e.equals(getBookNotFoundMessage(id)) ?
                        new ResponseStatusException(HttpStatus.NOT_FOUND) : new BookAppException(e));
        return ResponseEntity.ok().body(patchedBook);
    }

}
