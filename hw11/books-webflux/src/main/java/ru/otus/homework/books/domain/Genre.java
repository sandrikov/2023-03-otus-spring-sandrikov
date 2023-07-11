package ru.otus.homework.books.domain;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@Table(name = "genres")
@ToString
public class Genre {

    @Id
    private final Long id;

    @NotNull
    private final String name;

    @PersistenceCreator
    public Genre(Long id, @NotNull String name) {
        this.id = id;
        this.name = name;
    }

    public Genre(String name) {
        this(0L, name);
    }

}
