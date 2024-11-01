package com.example.demo.exception;

public class Authentication extends RuntimeException {
    public Authentication(String message) {
        super(message);
    }
}
