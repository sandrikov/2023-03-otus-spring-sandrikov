package ru.otus.homework.books.shell;

import org.jline.terminal.Terminal;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class SpringShellConfig {

    @Bean
    ShellHelper shellHelper(@Lazy Terminal terminal, @Lazy ShellColors shellColors) {
        return new ShellHelper(terminal, shellColors);
    }

}