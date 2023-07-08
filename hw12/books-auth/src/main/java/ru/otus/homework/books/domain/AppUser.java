package ru.otus.homework.books.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import static jakarta.persistence.CascadeType.ALL;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users")
public class AppUser {

    @Id
    private String username;

    private String password;

    private boolean enabled;

    @OneToMany(cascade = ALL, orphanRemoval = true, mappedBy = "user", fetch = FetchType.EAGER)
    private List<Authority> authorities;
}
