package ru.otus.homework.books.rest;

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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.homework.books.domain.Author;
import ru.otus.homework.books.mappers.AuthorMapper;
import ru.otus.homework.books.repository.AuthorRepository;
import ru.otus.homework.books.rest.dto.AuthorDto;
import ru.otus.homework.books.rest.misc.BookAppException;
import ru.otus.homework.books.rest.misc.ResponseUtil;
import ru.otus.homework.books.services.AuthorService;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;

import static ru.otus.homework.books.rest.misc.ResponseUtil.toURI;
import static ru.otus.homework.books.rest.misc.RestErrorMessages.getInvalidIdMessage;
import static ru.otus.homework.books.rest.misc.RestErrorMessages.getNewEntityHaveIdMessage;
import static ru.otus.homework.books.services.misc.ServiceErrorMessages.getAuthorNotFoundMessage;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Log4j2
public class AuthorRestController {

    private final AuthorRepository authorRepository;

    private final AuthorMapper authorMapper;

    @GetMapping("/authors")
    public Flux<AuthorDto> getAllAuthors() {
        log.debug("REST request to get all Authors");
        return authorRepository.findAll().map(authorMapper::toDto);
    }

    @GetMapping("/authors/{id}")
    public Mono<ResponseEntity<AuthorDto>> getAuthor(@PathVariable Long id) {
        log.debug("REST request to get Author : {}", id);
        return authorRepository.findById(id)
                .map(authorMapper::toDto)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.fromCallable(() -> ResponseEntity.notFound().build()));
    }

    @PostMapping("/authors")
    public Mono<ResponseEntity<AuthorDto>> createAuthor(@RequestBody Mono<AuthorDto> authorDto) throws URISyntaxException {
        log.debug("REST request to save Author");
        return authorRepository.save(authorDto.map(authorMapper::toEntity))
                .map(authorMapper::toDto)
                .map(entity -> ResponseEntity.created(toURI("/api/authors/" + entity.getId()))
                        .body(entity));
    }

    @PutMapping("/authors/{id}")
    public Mono<ResponseEntity<AuthorDto>> updateAuthor(@PathVariable(value = "id") final Long id,
                                              @RequestBody Mono<AuthorDto> authorDto) {
        log.debug("REST request to update Author : {}", id);
//        if (!Objects.equals(id, authorDto.getId())) {
//            throw new BookAppException(getInvalidIdMessage(id, authorDto.getId()));
//        }
        Mono<Author> authorMono = authorDto
                .filter(dto -> Objects.equals(id, authorDto.getId())
                .map(authorMapper::toEntity);
        val author = authorRepository.save(authorMono);
        val result = authorService.renameAuthor(id, author.getName()).orElseThrow(BookAppException::new);
        return ResponseEntity.ok().body(result);
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
            @RequestBody AuthorDto author) {
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
