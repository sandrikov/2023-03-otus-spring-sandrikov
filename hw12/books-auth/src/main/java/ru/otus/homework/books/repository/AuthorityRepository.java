package ru.otus.homework.books.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.homework.books.domain.Authority;

public interface AuthorityRepository extends JpaRepository<Authority, String> {
}
