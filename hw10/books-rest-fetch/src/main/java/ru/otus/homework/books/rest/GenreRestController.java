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
import ru.otus.homework.books.rest.dto.GenreDto;
import ru.otus.homework.books.rest.misc.ResponseUtil;
import ru.otus.homework.books.services.GenreService;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;

import static ru.otus.homework.books.rest.misc.RestErrorMessages.getInvalidIdMessage;
import static ru.otus.homework.books.rest.misc.RestErrorMessages.getNewEntityHaveIdMessage;
import static ru.otus.homework.books.services.misc.ServiceErrorMessages.getGenreNotFoundMessage;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Log4j2
public class GenreRestController {

    private final GenreService genreService;

    @GetMapping("/genres")
    public List<GenreDto> getAllGenres() {
        log.debug("REST request to get all Genres");
        var response = genreService.listGenres();
        return response.getData();
    }

    @PostMapping("/genres")
    public ResponseEntity<GenreDto> createGenre(@Valid @RequestBody GenreDto genre) throws URISyntaxException {
        log.debug("REST request to save Genre : {}", genre);
        if (genre.getId() != 0) {
            throw new BookAppException(getNewEntityHaveIdMessage("genre"));
        }
        val result = genreService.createGenre(genre.getName()).orElseThrow(BookAppException::new);
        return ResponseEntity
                .created(new URI("/api/genres/" + result.getId()))
                .body(result);
    }

    @PutMapping("/genres/{id}")
    public ResponseEntity<GenreDto> updateGenre(@PathVariable(value = "id") final Long id,
                                                  @Valid @RequestBody GenreDto genre) {
        log.debug("REST request to update Genre : {}, {}", id, genre);
        if (!Objects.equals(id, genre.getId())) {
            throw new BookAppException(getInvalidIdMessage(id, genre.getId()));
        }
        val result = genreService.renameGenre(id, genre.getName()).orElseThrow(BookAppException::new);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/genres/{id}")
    public ResponseEntity<GenreDto> getGenre(@PathVariable Long id) {
        log.debug("REST request to get Genre : {}", id);
        return ResponseUtil.wrapOrNotFound(genreService.getGenre(id));
    }

    @DeleteMapping("/genres/{id}")
    public ResponseEntity<Void> deleteGenre(@PathVariable Long id) {
        log.debug("REST request to delete Genre : {}", id);
        genreService.deleteGenre(id)
                .orElseThrow(e -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(value = "/genres/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<GenreDto> partialUpdateGenre(
            @PathVariable(value = "id") final Long id,
            @NotNull @RequestBody GenreDto genre) {
        log.debug("REST request to partial update Genre partially : {}, {}", id, genre);
        if (!Objects.equals(id, genre.getId())) {
            throw new BookAppException(getInvalidIdMessage(id, genre.getId()));
        }
        val patchedGenre = genreService.renameGenre(id, genre.getName())
                .orElseThrow(e -> e.equals(getGenreNotFoundMessage(id)) ?
                        new ResponseStatusException(HttpStatus.NOT_FOUND) : new BookAppException(e));
        return ResponseEntity.ok().body(patchedGenre);
    }

}
