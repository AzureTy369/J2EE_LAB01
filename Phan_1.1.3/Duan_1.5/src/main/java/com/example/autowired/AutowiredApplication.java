package com.example.autowired;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

public class AutowiredApplication {
    @Configuration
    @ComponentScan("com.example.autowired")
    static class Config {}

    public static void main(String[] args) {
        try (AnnotationConfigApplicationContext context =
                     new AnnotationConfigApplicationContext(Config.class)) {
            System.out.println(context.getBean(OrderService.class).checkout());
        }
    }
}
