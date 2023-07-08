package ru.otus.homework.books.mappers;

import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import ru.otus.homework.books.domain.AppUser;

@Service
public class UserMapperImpl implements UserMapper {

    private final AuthorityMapper authorityMapper;

    public UserMapperImpl(AuthorityMapper authorityMapper) {
        this.authorityMapper = authorityMapper;
    }

    @Override
    public User toUserDetail(AppUser user) {
        return new User(user.getUsername(), user.getPassword(), user.isEnabled(),
                true, true, true,
                user.getAuthorities().stream().map(authorityMapper::toGrantedAuthority).toList());
    }
}
