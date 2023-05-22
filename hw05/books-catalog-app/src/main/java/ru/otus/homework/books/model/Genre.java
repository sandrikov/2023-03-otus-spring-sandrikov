package ru.otus.homework.books.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Genre implements Serializable {

    private Long id;

    private String name;

    public Genre(String name) {
        this(null, name);
    }
}
