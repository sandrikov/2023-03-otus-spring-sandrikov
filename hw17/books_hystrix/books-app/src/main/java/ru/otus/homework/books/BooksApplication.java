package ru.otus.homework.books;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BooksApplication {

    public static void main(String[] args) {
        SpringApplication.run(BooksApplication.class);

        System.out.println("""
                Чтобы перейти на страницу сайта открывай: http://localhost:8080

                Resilience4J

                Пользователи:
                 - admin     - EDITOR,ADMIN
                 - adult     - USER
                 - child     - USER,CHILD
                 - librarian - EDITOR
                 - guest     - not enabled

                Пароль одинаковый: 'password'

                Роль EDITOR разрешает изменения.
                Роль CHILD скрывает книги "для взрослых".
                """);
    }

}