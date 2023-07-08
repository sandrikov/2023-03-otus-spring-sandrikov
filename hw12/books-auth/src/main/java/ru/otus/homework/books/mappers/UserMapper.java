package ru.otus.homework.books.mappers;

import org.springframework.security.core.userdetails.User;
import ru.otus.homework.books.domain.AppUser;

public interface UserMapper {
    User toUserDetail(AppUser entity);
}
