package ru.otus.homework.butterflygarden ;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import lombok.val;
import ru.otus.homework.butterflygarden.services.SupplierService;


@SpringBootApplication
public class ButterflyGarden {

    public static void main(String[] args) {
        val ctx = SpringApplication.run(ButterflyGarden.class, args);
        val supplierService = ctx.getBean(SupplierService.class);
        supplierService.startSupply();
    }

}