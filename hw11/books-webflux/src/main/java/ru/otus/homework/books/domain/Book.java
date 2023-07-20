package ru.otus.homework.books.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@Accessors(chain = true)
@Table(name = "books")
@ToString
public class Book {

    @Id
    private final Long id;

    @Size(max = 128)
    @NotBlank
    private final String title;

    private final Long authorId;

    private final Long genreId;

    @Transient
    private Author author;

    @Transient
    private Genre genre;

    @PersistenceCreator
    public Book(Long id, String title, Long authorId, Long genreId) {
        this.id = id;
        this.title = title;
        this.authorId = authorId;
        this.genreId = genreId;
    }

    public Book(Long id, String title, Author author, Genre genre) {
        this(id, title, author.getId(), genre.getId());
        this.author = author;
        this.genre = genre;
    }

    public Book(String title, Author author, Genre genre) {
        this(null, title, author, genre);
    }

}
