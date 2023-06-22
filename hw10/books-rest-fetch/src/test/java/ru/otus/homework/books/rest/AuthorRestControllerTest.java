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
import ru.otus.homework.books.rest.dto.AuthorDto;
import ru.otus.homework.books.rest.misc.BookAppAdvice;
import ru.otus.homework.books.services.AuthorService;

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
import static ru.otus.homework.books.services.misc.ServiceErrorMessages.getAuthorNotFoundMessage;


@WebMvcTest(AuthorRestController.class)
class AuthorRestControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private AuthorService authorService;
    private static final String ENTITY_API_URL = "/api/authors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Test
    void getAllAuthors() throws Exception {
        val authors = List.of(createAuthorDto(1), createAuthorDto(2));
        given(authorService.listAuthors()).willReturn(done(authors));
        mvc.perform(get(ENTITY_API_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(mapper.writeValueAsString(authors)));
    }

    @Test
    void createAuthor() throws Exception {
        val authorToCreate = createAuthorDto(1);
        authorToCreate.setId(0);
        val createdAuthor = createAuthorDto(1);
        given(authorService.createAuthor(any())).willReturn(done(createdAuthor));
        mvc.perform(post(ENTITY_API_URL)
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(authorToCreate)))
                .andExpect(status().isCreated())
                .andExpect(content().json(mapper.writeValueAsString(createdAuthor)));
    }

    @Test
    void createAuthorWithExistingId() throws Exception {
        val authorToCreate = createAuthorDto(1);
        val message = getNewEntityHaveIdMessage("author");
        mvc.perform(post(ENTITY_API_URL)
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(authorToCreate)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().json(expectedErrorBody(message)));
    }

    @Test
    void putExistingAuthor() throws Exception {
        val author = createAuthorDto(1);
        given(authorService.renameAuthor(author.getId(), author.getName())).willReturn(done(author));
        val json = mapper.writeValueAsString(author);
        mvc.perform(put(ENTITY_API_URL_ID, author.getId())
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().json(json));
    }

    @Test
    void putNonExistingAuthor() throws Exception {
        val author = createAuthorDto(1);
        val message = getAuthorNotFoundMessage(author.getId());
        given(authorService.renameAuthor(author.getId(), author.getName())).willReturn(error(message));

        mvc.perform(put(ENTITY_API_URL_ID, author.getId())
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(author)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().json(expectedErrorBody(message)));
    }

    @Test
    void getAuthor() throws Exception {
        val author = createAuthorDto(1);
        given(authorService.getAuthor(author.getId())).willReturn(done(author));
        mvc.perform(get(ENTITY_API_URL_ID, author.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(author.getId()))
                .andExpect(jsonPath("$.name").value(author.getName()));
    }

    @Test
    void getNonExistingAuthor() throws Exception {
        given(authorService.getAuthor(Long.MAX_VALUE))
                .willReturn(error(getAuthorNotFoundMessage(Long.MAX_VALUE)));
        mvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteAuthor() throws Exception {
        val author = createAuthorDto(1);
        given(authorService.deleteAuthor(author.getId()))
                .willReturn(done(author));
        mvc.perform(delete(ENTITY_API_URL_ID, author.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void fullUpdateAuthorWithPatch() throws Exception {
        val author = createAuthorDto(1);
        given(authorService.renameAuthor(author.getId(), author.getName())).willReturn(done(author));
        val json = mapper.writeValueAsString(author);
        mvc.perform(patch(ENTITY_API_URL_ID, author.getId())
                        .contentType("application/merge-patch+json")
                        .content(mapper.writeValueAsBytes(author)))
                .andExpect(status().isOk())
                .andExpect(content().json(json));
    }

    @Test
    void patchNonExistingAuthor() throws Exception {
        val author = createAuthorDto(99);
        val message = getAuthorNotFoundMessage(author.getId());
        given(authorService.renameAuthor(author.getId(), author.getName())).willReturn(error(message));
        mvc.perform(patch(ENTITY_API_URL_ID, author.getId())
                        .contentType("application/merge-patch+json")
                        .content(mapper.writeValueAsBytes(author)))
                .andExpect(status().isNotFound());
    }


    private static AuthorDto createAuthorDto(long id) {
        return new AuthorDto(id, "author" + id);
    }


    private String expectedErrorBody(String expectedErrorMessage) throws JsonProcessingException {
        return mapper.writeValueAsString(new BookAppAdvice.Response(expectedErrorMessage));
    }

}
