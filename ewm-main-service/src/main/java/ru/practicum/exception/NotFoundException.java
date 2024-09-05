package ru.practicum.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NotFoundException extends RuntimeException {
    private final Long id;
    private final Class<?> type;
}