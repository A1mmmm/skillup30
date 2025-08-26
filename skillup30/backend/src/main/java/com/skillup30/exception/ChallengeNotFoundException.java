package com.skillup30.exception;

public class ChallengeNotFoundException extends RuntimeException {
    public ChallengeNotFoundException(String message) {
        super(message);
    }

    public ChallengeNotFoundException(Long id) {
        super("Челлендж с ID '" + id + "' не найден");
    }
}