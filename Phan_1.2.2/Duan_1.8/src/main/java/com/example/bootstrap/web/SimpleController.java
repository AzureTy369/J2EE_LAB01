package com.example.bootstrap.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.bootstrap.persistence.repo.BookRepository;

// Controller trả về view Thymeleaf (templates/home.html), không phải JSON
@Controller
public class SimpleController {

    @Value("${spring.application.name}")
    private String appName;

    private final BookRepository bookRepository;

    public SimpleController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @GetMapping("/")
    public String homePage(Model model) {
        model.addAttribute("appName", appName);
        model.addAttribute("books", bookRepository.findAll());
        return "home";
    }
}
