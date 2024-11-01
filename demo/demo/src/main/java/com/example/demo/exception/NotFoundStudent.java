package com.example.demo.exception;

public class NotFoundStudent extends RuntimeException {
    public NotFoundStudent(String message) {
        super(message);
    }
}
