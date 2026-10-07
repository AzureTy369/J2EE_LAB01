package com.example.propertysource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppInfo {
    @Value("${app.name}") private String name;
    @Value("${app.owner}") private String owner;
    @Value("${app.description}") private String description;

    public String summary() {
        return name + " | owner=" + owner + " | " + description;
    }
}
