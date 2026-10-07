package com.example.container;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConf {
    @Bean
    public GreetingService greetingService() {
        return new GreetingService("Xin chào");
    }
}
