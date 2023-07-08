package ru.otus.homework.books.mappers;

import org.springframework.security.core.GrantedAuthority;
import ru.otus.homework.books.domain.Authority;

public interface AuthorityMapper {
    GrantedAuthority toGrantedAuthority(Authority authority);
}
