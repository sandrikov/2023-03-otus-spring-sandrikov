package ru.otus.homework.books.services;

import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import ru.otus.homework.books.mappers.UserMapper;
import ru.otus.homework.books.repository.AppUserRepository;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final AppUserRepository userRepository;

    private final UserMapper userMapper;

    public UserServiceImpl(AppUserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public Optional<User> getUserWithAuthorities(String username) {
        return userRepository.findById(username).map(userMapper::toUserDetail);
    }
}
