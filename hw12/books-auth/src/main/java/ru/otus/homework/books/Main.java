package ru.otus.homework.books;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class);

        System.out.println("""
                Чтобы перейти на страницу сайта открывай: http://localhost:8080

                Доступ к '/' публичный, '/book/**' требует авторизации.

                Пользователи:
                 - user:password  - USER
                 - admin:password - ADMIN,USER
                 - guest:password - not enabled
                """);
    }

}