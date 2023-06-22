package ru.otus.homework.books.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.homework.books.rest.dto.GenreDto;
import ru.otus.homework.books.rest.misc.BookAppAdvice;
import ru.otus.homework.books.services.GenreService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.otus.homework.books.rest.misc.RestErrorMessages.getNewEntityHaveIdMessage;
import static ru.otus.homework.books.services.ServiceResponse.done;
import static ru.otus.homework.books.services.ServiceResponse.error;
import static ru.otus.homework.books.services.misc.ServiceErrorMessages.getGenreNotFoundMessage;


@WebMvcTest(GenreRestController.class)
class GenreRestControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private GenreService genreService;
    private static final String ENTITY_API_URL = "/api/genres";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Test
    void getAllGenres() throws Exception {
        val genres = List.of(createGenreDto(1), createGenreDto(2));
        given(genreService.listGenres()).willReturn(done(genres));
        mvc.perform(get(ENTITY_API_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(mapper.writeValueAsString(genres)));
    }

    @Test
    void createGenre() throws Exception {
        val genreToCreate = createGenreDto(1);
        genreToCreate.setId(0);
        val createdGenre = createGenreDto(1);
        given(genreService.createGenre(any())).willReturn(done(createdGenre));
        mvc.perform(post(ENTITY_API_URL)
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(genreToCreate)))
                .andExpect(status().isCreated())
                .andExpect(content().json(mapper.writeValueAsString(createdGenre)));
    }

    @Test
    void createGenreWithExistingId() throws Exception {
        val genreToCreate = createGenreDto(1);
        val message = getNewEntityHaveIdMessage("genre");
        mvc.perform(post(ENTITY_API_URL)
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(genreToCreate)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().json(expectedErrorBody(message)));
    }

    @Test
    void putExistingGenre() throws Exception {
        val genre = createGenreDto(1);
        given(genreService.renameGenre(genre.getId(), genre.getName())).willReturn(done(genre));
        val json = mapper.writeValueAsString(genre);
        mvc.perform(put(ENTITY_API_URL_ID, genre.getId())
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().json(json));
    }

    @Test
    void putNonExistingGenre() throws Exception {
        val genre = createGenreDto(1);
        val message = getGenreNotFoundMessage(genre.getId());
        given(genreService.renameGenre(genre.getId(), genre.getName())).willReturn(error(message));

        mvc.perform(put(ENTITY_API_URL_ID, genre.getId())
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(genre)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().json(expectedErrorBody(message)));
    }

    @Test
    void getGenre() throws Exception {
        val genre = createGenreDto(1);
        given(genreService.getGenre(genre.getId())).willReturn(done(genre));
        mvc.perform(get(ENTITY_API_URL_ID, genre.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(genre.getId()))
                .andExpect(jsonPath("$.name").value(genre.getName()));
    }

    @Test
    void getNonExistingGenre() throws Exception {
        given(genreService.getGenre(Long.MAX_VALUE))
                .willReturn(error(getGenreNotFoundMessage(Long.MAX_VALUE)));
        mvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteGenre() throws Exception {
        val genre = createGenreDto(1);
        given(genreService.deleteGenre(genre.getId()))
                .willReturn(done(genre));
        mvc.perform(delete(ENTITY_API_URL_ID, genre.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void fullUpdateGenreWithPatch() throws Exception {
        val genre = createGenreDto(1);
        given(genreService.renameGenre(genre.getId(), genre.getName())).willReturn(done(genre));
        val json = mapper.writeValueAsString(genre);
        mvc.perform(patch(ENTITY_API_URL_ID, genre.getId())
                        .contentType("application/merge-patch+json")
                        .content(mapper.writeValueAsBytes(genre)))
                .andExpect(status().isOk())
                .andExpect(content().json(json));
    }

    @Test
    void patchNonExistingGenre() throws Exception {
        val genre = createGenreDto(99);
        val message = getGenreNotFoundMessage(genre.getId());
        given(genreService.renameGenre(genre.getId(), genre.getName())).willReturn(error(message));
        mvc.perform(patch(ENTITY_API_URL_ID, genre.getId())
                        .contentType("application/merge-patch+json")
                        .content(mapper.writeValueAsBytes(genre)))
                .andExpect(status().isNotFound());
    }


    private static GenreDto createGenreDto(long id) {
        return new GenreDto(id, "genre" + id);
    }

    private String expectedErrorBody(String expectedErrorMessage) throws JsonProcessingException {
        return mapper.writeValueAsString(new BookAppAdvice.Response(expectedErrorMessage));
    }
}