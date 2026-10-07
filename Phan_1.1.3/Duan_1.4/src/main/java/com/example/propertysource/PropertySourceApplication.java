package com.example.propertysource;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class PropertySourceApplication {
    public static void main(String[] args) {
        try (AnnotationConfigApplicationContext context =
                     new AnnotationConfigApplicationContext(AppConfig.class)) {
            System.out.println(context.getBean(AppInfo.class).summary());
        }
    }
}
