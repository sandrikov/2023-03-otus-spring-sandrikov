package ru.otus.homework.books.services;

import ru.otus.homework.books.model.Author;

import java.util.List;

public interface AuthorService {

    ServiceResponse<List<Author>> listAuthors();

    ServiceResponse<Author> getAuthor(long id);

    ServiceResponse<Author> findAuthor(String name);

    ServiceResponse<Author> createAuthor(String name);

    ServiceResponse<Author> renameAuthor(long id, String name);

    ServiceResponse<Author> deleteAuthor(long id);
}
