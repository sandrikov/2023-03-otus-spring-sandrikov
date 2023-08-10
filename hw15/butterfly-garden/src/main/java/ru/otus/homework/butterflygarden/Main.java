package ru.otus.homework.butterflygarden ;

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
                 - adult     - USER
                 - user      - USER,CHILD
                 - librarian - EDITOR
                 - admin     - EDITOR,ADMIN
                 - guest     - not enabled

                Пароль одинаковый: 'password'
                """);
    }

}