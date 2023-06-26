package ru.otus.homework.books.services.misc;

public interface ServiceErrorMessages {
    String AUTHOR_NOT_FOUND = "Author is not found: id=%d";

    String AUTHOR_NOT_FOUND_BY_NAME = "Author is not found: '%s'";

    String AUTHOR_ALREADY_EXISTS = "Author already exists: %s";

    String GENRE_NOT_FOUND = "Genre is not found: id=%d";

    String GENRE_NOT_FOUND_BY_NAME = "Genre is not found: '%s'";

    String GENRE_ALREADY_EXISTS = "Genre already exists: %s";

    String BOOK_NOT_FOUND = "Book is not found: id=%d";

    String BOOK_ALREADY_EXISTS = "Book already exists: %s '%s'";

    String COMMENT_NOT_FOUND = "Comment is not found: id=%d";

    static String getAuthorNotFoundMessage(long id) {
        return String.format(AUTHOR_NOT_FOUND, id);
    }

    static String getAuthorNotFoundMessage(String name) {
        return String.format(AUTHOR_NOT_FOUND_BY_NAME, name);
    }

    static String getAuthorAlreadyExistsMessage(String name) {
        return String.format(AUTHOR_ALREADY_EXISTS, name);
    }

    static String getGenreAlreadyExistsMessage(String name) {
        return String.format(GENRE_ALREADY_EXISTS, name);
    }

    static String getGenreNotFoundMessage(long id) {
        return String.format(GENRE_NOT_FOUND, id);
    }

    static String getGenreNotFoundMessage(String name) {
        return String.format(GENRE_NOT_FOUND_BY_NAME, name);
    }

    static String getBookNotFoundMessage(long id) {
        return String.format(BOOK_NOT_FOUND, id);
    }

    static String getBookAlreadyExistsMessage(String authorName, String title) {
        return String.format(BOOK_ALREADY_EXISTS, authorName, title);
    }

    static String getCommentNotFoundMessage(long id) {
        return String.format(COMMENT_NOT_FOUND, id);
    }
}
