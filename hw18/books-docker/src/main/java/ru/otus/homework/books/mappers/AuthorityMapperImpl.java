package ru.otus.homework.books.mappers;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import ru.otus.homework.books.domain.Authority;

@Service
public class AuthorityMapperImpl implements AuthorityMapper {
    @Override
    public GrantedAuthority toGrantedAuthority(Authority authority) {
        return new SimpleGrantedAuthority(authority.getAuthority());
    }
}
