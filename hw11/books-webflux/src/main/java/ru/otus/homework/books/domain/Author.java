package ru.otus.homework.books.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@Table(name = "authors")
@ToString
public class Author {

    @Id
    private final Long id;

    @Size(max = 128)
    @NotBlank
    private final String name;

    @PersistenceCreator
    public Author(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Author(String name) {
        this(null, name);
    }

}
