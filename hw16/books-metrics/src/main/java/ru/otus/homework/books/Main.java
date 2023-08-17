package ru.otus.homework.books;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class);

        System.out.println("""

                Cтраница сайта           : http://localhost:8080

                Аctuator                 : http://localhost:8080/monitor                       (!) admin only
                Logfile                  : http://localhost:8080/monitor/logfile               (!) admin only
                Healthchecks             : http://localhost:8080/monitor/health
                Собственный HealthCheck  : http://localhost:8080/monitor/health/authenticated
                 - UP - авторизованный доступ
                 - UNKNOWN - анонимный доступ
                HAL explorer             : http://localhost:8080/hal                           (!) admin only

                Пользователи:
                 - admin     - EDITOR,ADMIN
                 - adult     - USER
                 - user      - USER,CHILD
                 - librarian - EDITOR
                 - guest     - not enabled

                Пароль одинаковый: 'password'
                """);
    }

}