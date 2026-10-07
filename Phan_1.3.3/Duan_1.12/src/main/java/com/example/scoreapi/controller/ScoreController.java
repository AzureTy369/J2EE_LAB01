package com.example.scoreapi.controller;

import com.example.scoreapi.model.StudentScore;
import com.example.scoreapi.service.ScoreService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/diem")
@Validated
public class ScoreController {
    private final ScoreService scoreService;

    public ScoreController(ScoreService scoreService) {
        this.scoreService = scoreService;
    }

    @GetMapping(value = "/tra-cuu", produces = MediaType.APPLICATION_JSON_VALUE)
    public StudentScore lookup(
            @RequestParam("SBD") @NotBlank(message = "SBD không được để trống") String sbd,
            @RequestHeader(value = "User-Token", required = false) String headerToken,
            @RequestParam(value = "User-Token", required = false) String queryToken) {
        String userToken = headerToken != null ? headerToken : queryToken;
        return scoreService.findBySbd(sbd.trim(), userToken);
    }
}
