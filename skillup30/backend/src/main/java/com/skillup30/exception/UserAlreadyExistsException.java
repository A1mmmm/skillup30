package com.skillup30.exception;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }

    public UserAlreadyExistsException(String username, String email) {
        super("Пользователь с именем '" + username + "' или email '" + email + "' уже существует");
    }
}