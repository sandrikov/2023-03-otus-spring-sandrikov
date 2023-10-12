package ru.otus.coursework.staff;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StaffApplication {

    public static void main(String[] args) {
        SpringApplication.run(StaffApplication.class, args);
        System.out.println("""
                
                Включается: artifactId: spring-boot-starter-data-rest
                
                Метаданные про REST API
                http://localhost:8080/api/profile

                Прямые обращения :
                http://localhost:8080/api/employee?page=2&size=5
                http://localhost:8080/api/job?page=2&size=4&sort=jobTitle
                
                HAL Explorer (artifactId:spring-data-rest-hal-explorer)
                http://localhost:8080/api

                """);
    }

    /*
                Пользователи:
                 - admin      - ADMIN,EDIT
                 - user       - VIEW
                 - accountant - VIEW,FIN
                 - boss       - EDIT,FIN
                 - hr         - EDIT
                Пароль одинаковый: 'password'

                http://localhost:8080/actuator
     */
}
