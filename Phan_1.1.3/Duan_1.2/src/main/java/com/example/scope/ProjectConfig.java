package com.example.scope;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class ProjectConfig {
    @Bean
    @Scope("prototype")
    public VehicleServices vehicleServices() {
        return new VehicleServices();
    }
}
