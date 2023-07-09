package ru.otus.homework.books.domain;

import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "authors")
@ToString
public class Author {

    @Id
    private final Long id;

    @NotNull
    private final String name;

    @PersistenceCreator
    public Author(Long id, @NotNull String name) {
        this.id = id;
        this.name = name;
    }

    public Author(String name) {
        this(null, name);
    }

}
