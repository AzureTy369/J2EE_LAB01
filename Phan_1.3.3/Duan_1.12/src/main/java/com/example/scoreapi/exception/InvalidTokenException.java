package com.example.scoreapi.exception;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException() {
        super("User-Token không hợp lệ");
    }
}
