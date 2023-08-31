package ru.otus.homework.books;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class);

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