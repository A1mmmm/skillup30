package com.skillup30.exception;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String message) {
        super(message);
    }
    
    public InvalidTokenException() {
        super("Невалидный токен");
    }
} 