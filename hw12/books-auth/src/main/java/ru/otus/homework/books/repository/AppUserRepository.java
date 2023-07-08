package ru.otus.homework.books.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.homework.books.domain.AppUser;

public interface AppUserRepository extends JpaRepository<AppUser, String> {
}
