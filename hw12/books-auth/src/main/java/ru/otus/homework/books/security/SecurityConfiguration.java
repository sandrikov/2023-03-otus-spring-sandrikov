package ru.otus.homework.books.security;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import ru.otus.homework.books.services.UserService;

import static java.util.concurrent.TimeUnit.HOURS;
import static org.springframework.security.config.http.SessionCreationPolicy.ALWAYS;

@Configuration
@EnableWebSecurity
@Log4j2
public class SecurityConfiguration {


    @Bean
    public UserDetailsService userDetailsService(UserService userService) {
        return (String username) -> userService.getUserWithAuthorities(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf((csrf) -> csrf.disable())
                .sessionManagement((sm) -> sm.sessionCreationPolicy(ALWAYS))
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/book/**").authenticated()
                        .anyRequest().authenticated()
                )
                .formLogin((formLogin) -> formLogin
                        .permitAll()
                )
                .rememberMe((rememberMe) -> rememberMe
                        .key("bookAppKey")
                        .tokenValiditySeconds((int)HOURS.toSeconds(1))
                );
        return http.build();
    }
}
