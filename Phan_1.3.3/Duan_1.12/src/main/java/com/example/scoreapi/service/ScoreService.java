package com.example.scoreapi.service;

import com.example.scoreapi.exception.InvalidTokenException;
import com.example.scoreapi.exception.ScoreNotFoundException;
import com.example.scoreapi.model.StudentScore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ScoreService {
    private final Map<String, StudentScore> scores;
    private final String validToken;

    public ScoreService(
            @Value("${score-api.user-token:demo-token-123}") String validToken) {
        this.validToken = validToken;
        this.scores = loadScores();
    }

    public StudentScore findBySbd(String sbd, String userToken) {
        if (userToken == null || !userToken.equals(validToken)) {
            throw new InvalidTokenException();
        }

        StudentScore score = scores.get(sbd);
        if (score == null) {
            throw new ScoreNotFoundException(sbd);
        }
        return score;
    }

    private Map<String, StudentScore> loadScores() {
        ClassPathResource resource = new ClassPathResource("data/diem-thi.csv");
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
            return reader.lines()
                    .skip(1)
                    .filter(line -> !line.isBlank())
                    .map(this::parseLine)
                    .collect(Collectors.toUnmodifiableMap(StudentScore::sbd, Function.identity()));
        } catch (IOException e) {
            throw new IllegalStateException("Không thể đọc dữ liệu điểm thi", e);
        }
    }

    private StudentScore parseLine(String line) {
        String[] columns = line.split(",", -1);
        if (columns.length != 5) {
            throw new IllegalStateException("Dòng dữ liệu điểm thi không hợp lệ: " + line);
        }
        return new StudentScore(
                columns[0].trim(),
                columns[1].trim(),
                new BigDecimal(columns[2].trim()),
                new BigDecimal(columns[3].trim()),
                new BigDecimal(columns[4].trim()));
    }
}
