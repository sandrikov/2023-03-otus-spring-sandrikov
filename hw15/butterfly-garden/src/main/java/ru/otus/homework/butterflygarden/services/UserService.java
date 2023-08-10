package ru.otus.homework.butterflygarden .services;

import org.springframework.security.core.userdetails.User;

import java.util.Optional;

public interface UserService {
    Optional<User> getUserWithAuthorities(String username);
}
