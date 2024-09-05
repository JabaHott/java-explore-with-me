package ru.practicum.exception;

import jakarta.validation.UnexpectedTypeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.category.model.Category;
import ru.practicum.comment.model.Comment;
import ru.practicum.compilations.model.CompilationsEntity;
import ru.practicum.event.model.Event;
import ru.practicum.request.model.RequestEntity;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class ExceptionController {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handle(final NotFoundException e) {
        ErrorResponse errorResponse = null;
        Long id = e.getId();
        if (e.getType().equals(Category.class)) {
            log.error("Получен статус 404 Not found {}", e.getMessage(), e);
            errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.toString(), "The required object was not found.",
                    String.format("Category with id=%s was not found", id), LocalDateTime.now());
        } else if (e.getType().equals(User.class)) {
            log.error("Получен статус 404 Not found {}", e.getMessage(), e);
            errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.toString(), "The required object was not found.",
                    String.format("User with id=%s was not found", id), LocalDateTime.now());
        } else if (e.getType().equals(Event.class)) {
            log.error("Получен статус 404 Not found {}", e.getMessage(), e);
            errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.toString(), "The required object was not found.",
                    String.format("Event with id=%s was not found", id), LocalDateTime.now());
        } else if (e.getType().equals(RequestEntity.class)) {
            log.error("Получен статус 404 Not found {}", e.getMessage(), e);
            errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.toString(), "The required object was not found.",
                    String.format("Request with id=%s was not found", id), LocalDateTime.now());
        } else if (e.getType().equals(CompilationsEntity.class)) {
            log.error("Получен статус 404 Not found {}", e.getMessage(), e);
            errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.toString(), "The required object was not found.",
                    String.format("Compilation with id=%s was not found", id), LocalDateTime.now());
        } else if (e.getType().equals(Comment.class)) {
            log.error("Получен статус 404 Not found {}", e.getMessage(), e);
            errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.toString(), "The required object was not found.",
                    String.format("Comment with id=%s was not found", id), LocalDateTime.now());
        }
        return errorResponse;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handle(final DataIntegrityViolationException e) {
        String message = e.getMessage();
        log.error("Ошибка уникальности!");
        return new ErrorResponse(HttpStatus.CONFLICT.toString(), "Integrity constraint has been violated.",
                message, LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handle(final IncorrectEventStateException e) {
        log.error("Условия не соблюдены!");
        return new ErrorResponse(HttpStatus.FORBIDDEN.toString(), "For the requested operation the conditions are not met.",
                "Only pending or canceled events can be changed", LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handle(final RequestException e) {
        String message = e.getMessage();
        String description = e.getDescription();
        log.error(message);
        return new ErrorResponse(HttpStatus.CONFLICT.toString(), message, description, LocalDateTime.now());
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handle(final ValidationException e) {
        log.error("Ошибка валидации для списка категорий!");
        return new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), "Ошибка валидации для списка категорий!",
                "Ошибка валидации для списка категорий!", LocalDateTime.now());
    }

    @ExceptionHandler(UnexpectedTypeException.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedTypeException(UnexpectedTypeException e) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), "Ошибка",
                "Ошибка", LocalDateTime.now());
        return ResponseEntity.badRequest().body(errorResponse);

    }
}
