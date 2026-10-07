package com.example.container;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.context.ConfigurableApplicationContext;

public class GreetingApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(AppConf.class);
        application.setWebApplicationType(WebApplicationType.NONE);
        try (ConfigurableApplicationContext context = application.run(args)) {
            GreetingService greetingService = context.getBean(GreetingService.class);
            System.out.println(greetingService.greet("Spring Container"));
            System.out.println("Bean name: greetingService");
            System.out.println("Bean type: " + greetingService.getClass().getSimpleName());
        }
    }
}
