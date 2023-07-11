package ru.otus.homework.books.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@Table(name = "books")
@ToString
public class Book {

    @Id
    private final Long id;

    @NotNull
    private final String title;

    @NotNull
    @Column("author_id")
    private final Author author;

    @NotNull
    @Column("genre_id")
    private final Genre genre;


    @PersistenceCreator
    public Book(Long id, String title, Author author, Genre genre) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.genre = genre;
    }

    public Book(String title, Author author, Genre genre) {
        this(null, title, author, genre);
    }

}
