package ru.otus.homework.books.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public final class AuthUtils {

    private static final String ROLE_PREFIX = "ROLE_";

    public static boolean hasRole(String role) {
        return Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getAuthorities)
                .map(authorities -> authorities.stream().map(GrantedAuthority::getAuthority)
                        .anyMatch(a -> a.startsWith(ROLE_PREFIX)
                                && role.equals(a.substring(ROLE_PREFIX.length()))))
                .orElse(false);
    }
}
