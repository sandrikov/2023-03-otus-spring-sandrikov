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
import ru.otus.homework.books.rest.dto.AuthorDto;
import ru.otus.homework.books.rest.misc.ResponseUtil;
import ru.otus.homework.books.services.AuthorService;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;

import static ru.otus.homework.books.rest.misc.RestErrorMessages.getInvalidIdMessage;
import static ru.otus.homework.books.rest.misc.RestErrorMessages.getNewEntityHaveIdMessage;
import static ru.otus.homework.books.services.misc.ServiceErrorMessages.getAuthorNotFoundMessage;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Log4j2
public class AuthorRestController {

    private final AuthorService authorService;

    @GetMapping("/authors")
    public List<AuthorDto> getAllAuthors() {
        log.debug("REST request to get all Authors");
        var response = authorService.listAuthors();
        return response.getData();
    }

    @PostMapping("/authors")
    public ResponseEntity<AuthorDto> createAuthor(@Valid @RequestBody AuthorDto author) throws URISyntaxException {
        log.debug("REST request to save Author : {}", author);
        if (author.getId() != 0) {
            throw new BookAppException(getNewEntityHaveIdMessage("author"));
        }
        val result = authorService.createAuthor(author.getName()).orElseThrow(BookAppException::new);
        return ResponseEntity
                .created(new URI("/api/authors/" + result.getId()))
                .body(result);
    }

    @PutMapping("/authors/{id}")
    public ResponseEntity<AuthorDto> updateAuthor(@PathVariable(value = "id") final Long id,
                                              @Valid @RequestBody AuthorDto author) {
        log.debug("REST request to update Author : {}, {}", id, author);
        if (!Objects.equals(id, author.getId())) {
            throw new BookAppException(getInvalidIdMessage(id, author.getId()));
        }
        val result = authorService.renameAuthor(id, author.getName()).orElseThrow(BookAppException::new);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/authors/{id}")
    public ResponseEntity<AuthorDto> getAuthor(@PathVariable Long id) {
        log.debug("REST request to get Author : {}", id);
        return ResponseUtil.wrapOrNotFound(authorService.getAuthor(id));
    }

    @DeleteMapping("/authors/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {
        log.debug("REST request to delete Author : {}", id);
        authorService.deleteAuthor(id)
                .orElseThrow(e -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(value = "/authors/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AuthorDto> partialUpdateAuthor(
            @PathVariable(value = "id") final Long id,
            @NotNull @RequestBody AuthorDto author) {
        log.debug("REST request to partial update Author partially : {}, {}", id, author);
        if (!Objects.equals(id, author.getId())) {
            throw new BookAppException(getInvalidIdMessage(id, author.getId()));
        }
        val patchedAuthor = authorService.renameAuthor(id, author.getName())
                .orElseThrow(e -> e.equals(getAuthorNotFoundMessage(id)) ?
                        new ResponseStatusException(HttpStatus.NOT_FOUND) : new BookAppException(e));
        return ResponseEntity.ok().body(patchedAuthor);
    }
    
}
