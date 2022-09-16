package ru.job4j.chat.exception;

public class RoleByNameExistsException extends RuntimeException {
    public RoleByNameExistsException(String message) {
        super(message);
    }
}
