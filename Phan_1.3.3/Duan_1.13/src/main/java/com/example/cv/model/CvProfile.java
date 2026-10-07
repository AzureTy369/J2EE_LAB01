package com.example.cv.model;

import java.util.List;

public record CvProfile(
        String name,
        String role,
        String summary,
        String email,
        String phone,
        String location,
        String github,
        List<String> skills,
        List<Experience> experiences,
        List<Project> projects) {

    public record Experience(String period, String company, String position, String description) {
    }

    public record Project(String name, String description, String technologies, String link) {
    }
}
