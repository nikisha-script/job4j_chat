package ru.job4j.chat.exception;

public class UserByLoginExistsException extends RuntimeException {
    public UserByLoginExistsException(String message) {
        super(message);
    }
}
