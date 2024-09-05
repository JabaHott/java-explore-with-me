package ru.practicum.exception;

public class IncorrectDateException extends IllegalArgumentException {
    public IncorrectDateException(String message) {
        super(message);
    }
}
