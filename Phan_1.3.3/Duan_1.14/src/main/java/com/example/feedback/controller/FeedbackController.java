package com.example.feedback.controller;

import com.example.feedback.model.FeedbackForm;
import com.example.feedback.service.FeedbackService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class FeedbackController {
    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @GetMapping("/")
    public String form(Model model,
                       @RequestParam(value = "sent", defaultValue = "false") boolean sent) {
        model.addAttribute("feedback", new FeedbackForm());
        model.addAttribute("sent", sent);
        return "feedback";
    }

    @PostMapping("/feedback")
    public String submit(@Valid @ModelAttribute("feedback") FeedbackForm feedback,
                         BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("sent", false);
            return "feedback";
        }
        feedbackService.save(feedback);
        return "redirect:/?sent=true";
    }
}
