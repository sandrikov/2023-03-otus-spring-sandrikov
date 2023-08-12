package ru.otus.homework.butterflygarden ;

import lombok.val;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.otus.homework.butterflygarden.services.SupplierService;


@SpringBootApplication
public class ButterflyGardenApp {

    public static void main(String[] args) {
        val ctx = SpringApplication.run(ButterflyGardenApp.class, args);
        val supplierService = ctx.getBean(SupplierService.class);
        supplierService.startSupply();
    }

}