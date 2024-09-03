package ru.practicum.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RequestException extends IllegalArgumentException {
    private String message;
    private String description;
}