package com.skillup30.exception;

public class UserNotJoinedChallengeException extends RuntimeException {
    public UserNotJoinedChallengeException(String message) {
        super(message);
    }
    
    public UserNotJoinedChallengeException(String username, Long challengeId) {
        super("Пользователь '" + username + "' не участвует в челлендже с ID '" + challengeId + "'");
    }
} 