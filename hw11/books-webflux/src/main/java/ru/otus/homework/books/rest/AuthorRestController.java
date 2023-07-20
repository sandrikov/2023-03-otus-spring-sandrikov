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
import ru.otus.homework.books.rest.dto.AuthorDto;
import ru.otus.homework.books.services.AuthorService;

import static org.springframework.http.ResponseEntity.notFound;
import static ru.otus.homework.books.rest.misc.ResponseUtil.toURI;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Log4j2
public class AuthorRestController {

    private final AuthorService authorService;

    @GetMapping("/authors")
    public Flux<AuthorDto> getAllAuthors() {
        log.debug("REST request to get all Authors");
        return authorService.listAuthors();
    }

    @GetMapping("/authors/{id}")
    public Mono<ResponseEntity<AuthorDto>> getAuthor(@PathVariable Long id) {
        log.debug("REST request to get Author : {}", id);
        return authorService.getAuthor(id).map(ResponseEntity::ok)
                .switchIfEmpty(Mono.fromCallable(() -> notFound().build()));
    }

    @PostMapping("/authors")
    public Mono<ResponseEntity<AuthorDto>> createAuthor(@RequestBody Mono<AuthorDto> authorDto) {
        log.debug("REST request to save Author");
        return authorService.createAuthor(authorDto)
                .map(dto -> ResponseEntity.created(toURI("/api/authors/" + dto.getId())).body(dto));
    }

    @PutMapping("/authors/{id}")
    public Mono<ResponseEntity<AuthorDto>> updateAuthor(@PathVariable(value = "id") final Long id,
                                                        @RequestBody Mono<AuthorDto> authorDto) {
        log.debug("REST request to update Author : {}", id);
        return authorService.updateAuthor(id, authorDto)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.fromCallable(() -> notFound().build()));
    }

    @DeleteMapping("/authors/{id}")
    public Mono<ResponseEntity<Void>> deleteAuthor(@PathVariable Long id) {
        log.debug("REST request to delete Author : {}", id);
        return authorService.deleteAuthor(id)
                .then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK)))
                .switchIfEmpty(Mono.fromCallable(() -> notFound().build()));
    }

}
