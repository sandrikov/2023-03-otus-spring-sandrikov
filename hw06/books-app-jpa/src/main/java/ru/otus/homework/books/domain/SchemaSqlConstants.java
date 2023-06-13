package ru.otus.homework.books.domain;

public interface SchemaSqlConstants {

    int MAX_TITLE_LENGTH = 128;

    int MAX_NAME_LENGTH = 128;

    int MAX_TEXT_LENGTH = 1024;

    String UK_AUTHOR_NAME = "UK_author_name";

    String UK_BOOK_TITLE_AUTHOR = "UK_book_title_author";

    String FK_BOOK_AUTHOR = "FK_book_author";

    String FK_BOOK_GENRE = "FK_book_genre";

    String UK_GENRE_NAME = "UK_genre_name";
}
