package ru.otus.homework.books.monitor;

import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Log4j2
@Component
public class AuthenticatedHealthIndicator implements HealthIndicator {
    @Override
    public Health health() {
        val authentication = Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .filter(t -> t.isAuthenticated() && t.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority).noneMatch("ROLE_ANONYMOUS"::equals));
        if (authentication.isPresent()) {
            log.info("AuthenticatedHealthIndicator: Authenticated access");
            return Health.up().withDetail("UserInfo", authentication.get().getPrincipal()).build();
        }
        log.info("AuthenticatedHealthIndicator: Anonymous access");
        return Health.unknown().build();
    }
}
