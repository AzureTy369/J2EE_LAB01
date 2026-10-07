package com.example.customscope;

import org.springframework.beans.factory.config.CustomScopeConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.Map;

@Configuration
public class CustomScopeConfig {
    @Bean
    public static CustomScopeConfigurer customScopes() {
        CustomScopeConfigurer configurer = new CustomScopeConfigurer();
        configurer.setScopes(Map.of("thread", new ThreadScope()));
        return configurer;
    }

    @Bean
    @Scope("thread")
    public RequestContext requestContext() {
        return new RequestContext();
    }
}
