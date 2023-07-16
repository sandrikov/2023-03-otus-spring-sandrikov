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
import ru.otus.homework.books.rest.dto.GenreDto;
import ru.otus.homework.books.services.GenreService;

import static org.springframework.http.ResponseEntity.notFound;
import static ru.otus.homework.books.rest.misc.ResponseUtil.toURI;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Log4j2
public class GenreRestController {

    private final GenreService genreService;

    @GetMapping("/genres")
    public Flux<GenreDto> getAllGenres() {
        log.debug("REST request to get all Genres");
        return genreService.listGenres();
    }

    @GetMapping("/genres/{id}")
    public Mono<ResponseEntity<GenreDto>> getGenre(@PathVariable Long id) {
        log.debug("REST request to get Genre : {}", id);
        return genreService.getGenre(id).map(ResponseEntity::ok)
                .switchIfEmpty(Mono.fromCallable(() -> notFound().build()));
    }

    @PostMapping("/genres")
    public Mono<ResponseEntity<GenreDto>> createGenre(@RequestBody Mono<GenreDto> genreDto) {
        log.debug("REST request to save Genre");
        return genreService.createGenre(genreDto)
                .map(dto -> ResponseEntity.created(toURI("/api/genres/" + dto.getId())).body(dto));
    }

    @PutMapping("/genres/{id}")
    public Mono<ResponseEntity<GenreDto>> updateGenre(@PathVariable(value = "id") final Long id,
                                                      @RequestBody Mono<GenreDto> genreDto) {
        log.debug("REST request to update Genre : {}", id);
        return genreService.updateGenre(id, genreDto)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.fromCallable(() -> notFound().build()));
    }

    @DeleteMapping("/genres/{id}")
    public Mono<ResponseEntity<Void>> deleteGenre(@PathVariable Long id) {
        log.debug("REST request to delete Genre : {}", id);
        return genreService.deleteGenre(id)
                .then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK)))
                .switchIfEmpty(Mono.fromCallable(() -> notFound().build()));
    }

}
