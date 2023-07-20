package ru.otus.homework.books.services;

import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.GrantedAuthority;
import ru.otus.homework.books.mappers.AuthorMapper;
import ru.otus.homework.books.mappers.AuthorityMapperImpl;
import ru.otus.homework.books.mappers.BookMapper;
import ru.otus.homework.books.mappers.CommentMapper;
import ru.otus.homework.books.mappers.GenreMapper;
import ru.otus.homework.books.mappers.UserMapperImpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("API для работы с пользователями. Интеграционный тест")
@DataJpaTest
@MockBean({GenreMapper.class, AuthorMapper.class, BookMapper.class, CommentMapper.class})
@Import({UserServiceImpl.class, UserMapperImpl.class, AuthorityMapperImpl.class})
class UserServiceTest {
    @Autowired
    private UserService userService;

    @Test
    void getUserByWrongUsername() {
        val user = userService.getUserWithAuthorities("wrong_username");
        assertFalse(user.isPresent(), "Username not found");
    }

    @Test
    void getUserWithAuthority() {
        val res = userService.getUserWithAuthorities("userOneAuthority");
        assertTrue(res.isPresent(), "Username is found");
        val user = res.get();
        assertTrue(user.isEnabled(), "Enabled");
        assertThat(user.getAuthorities()).isNotNull()
                .hasSize(1)
                .element(0).matches(a -> a.getAuthority().equals("ROLE_USER"), "Has role 'USER'");
    }

    @Test
    void getUserWithTwoAuthorities() {
        val res = userService.getUserWithAuthorities("userTwoAuthorities");
        assertTrue(res.isPresent(), "Username is found");
        val user = res.get();
        assertTrue(user.isEnabled(), "Enabled");
        assertThat(user.getAuthorities()).isNotNull()
                .hasSize(2)
                .map(GrantedAuthority::getAuthority)
                .contains("ROLE_USER", "ROLE_ADMIN");
    }


    @Test
    void getUserWithoutAuthorities() {
        val res = userService.getUserWithAuthorities("userNoAuthority");
        assertTrue(res.isPresent(), "Username is found");
        val user = res.get();
        assertFalse(user.isEnabled(), "Not enabled");
        assertThat(user.getAuthorities()).isNotNull().hasSize(0);
    }

}