package com.example.bootstrap.web.exception;

public class BookIdMismatchException extends RuntimeException {

    public BookIdMismatchException() {
        super("Book id in URL does not match book id in body");
    }
}
