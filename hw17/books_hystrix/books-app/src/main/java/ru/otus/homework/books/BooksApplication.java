package ru.otus.homework.books;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;

@SpringBootApplication
@EnableCircuitBreaker
public class BooksApplication {

    public static void main(String[] args) {
        SpringApplication.run(BooksApplication.class);

        System.out.println("""
                Чтобы перейти на страницу сайта открывай: http://localhost:8080

                Роль EDITOR разрешает изменения.
                Роль CHILD скрывает книги "для взрослых".

                Пользователи:
                 - admin     - EDITOR,ADMIN
                 - adult     - USER
                 - child     - USER,CHILD
                 - librarian - EDITOR
                 - guest     - not enabled

                Пароль одинаковый: 'password'
                """);
    }

}