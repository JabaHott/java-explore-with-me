package ru.practicum.exception;

public class IncorrectEventStateException extends RuntimeException {
    public IncorrectEventStateException(String message) {
        super(message);
    }
}
