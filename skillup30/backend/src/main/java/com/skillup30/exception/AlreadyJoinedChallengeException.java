package com.skillup30.exception;

public class AlreadyJoinedChallengeException extends RuntimeException {
    public AlreadyJoinedChallengeException(String message) {
        super(message);
    }

    public AlreadyJoinedChallengeException(String username, Long challengeId) {
        super("Пользователь '" + username + "' уже участвует в челлендже с ID '" + challengeId + "'");
    }
}