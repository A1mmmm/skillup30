package com.skillup30.exception;

public class AlreadyCheckedInException extends RuntimeException {
    public AlreadyCheckedInException(String message) {
        super(message);
    }

    public AlreadyCheckedInException(String username, Long challengeId) {
        super("Пользователь '" + username + "' уже отметил день для челленджа с ID '" + challengeId + "'");
    }
}