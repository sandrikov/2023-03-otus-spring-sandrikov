package ru.otus.coursework.staff;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StaffApplication {

    public static void main(String[] args) {
        SpringApplication.run(StaffApplication.class, args);
        System.out.println("""
                
                UI для управления сотрудниками: http://localhost:8080
                
                HAL Explorer:                   http://localhost:8080/api

                """);
    }

}
