package com.example.feedback.service;

import com.example.feedback.model.FeedbackForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class FeedbackService {
    private static final Logger log = LoggerFactory.getLogger(FeedbackService.class);

    public void save(FeedbackForm feedback) {
        log.info("Received service feedback: rating={}, topic={}, name={}, email={}, submittedAt={}",
                feedback.getRating(), feedback.getTopic(),
                feedback.getName(), feedback.getEmail(), Instant.now());
    }
}
