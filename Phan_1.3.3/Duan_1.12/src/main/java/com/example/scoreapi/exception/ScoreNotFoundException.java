package com.example.scoreapi.exception;

public class ScoreNotFoundException extends RuntimeException {
    public ScoreNotFoundException(String sbd) {
        super("Không tìm thấy thí sinh có SBD: " + sbd);
    }
}
