package ru.otus.homework.books.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static jakarta.persistence.GenerationType.IDENTITY;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "genres")
public class Genre {
    public static final int MAX_NAME_LENGTH = 128;

    public static final String UK_GENRE_NAME = "UK_genre_name";

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private long id;

    @Column(nullable = false, length = MAX_NAME_LENGTH)
    private String name;

    public Genre(String name) {
        this(0, name);
    }

}
