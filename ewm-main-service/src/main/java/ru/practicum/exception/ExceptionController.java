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
import ru.practicum.compilations.model.CompilationsEntity;
import ru.practicum.event.model.Event;
import ru.practicum.request.model.RequestEntity;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestControllerAdvice
@Slf4j
public class ExceptionController {
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handle(final NotFoundException e) {
        ErrorResponse errorResponse = null;
        Long id = e.getId();
        if (e.getObject() instanceof Category) {
            log.error(String.format("Категория с id = %s не зарегистрирована!", id));
            errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.toString(), "The required object was not found.",
                    String.format("Category with id=%s was not found", id), LocalDateTime.now().format(formatter));
        } else if (e.getObject() instanceof User) {
            log.error(String.format("Пользователь с id = %s не зарегистрирован", id));
            errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.toString(), "The required object was not found.",
                    String.format("User with id=%s was not found", id), LocalDateTime.now().format(formatter));
        } else if (e.getObject() instanceof Event) {
            log.error(String.format("Событие с id = %s не зарегистрировано", id));
            errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.toString(), "The required object was not found.",
                    String.format("Event with id=%s was not found", id), LocalDateTime.now().format(formatter));
        } else if (e.getObject() instanceof RequestEntity) {
            log.error(String.format("Заявка с id = %s не зарегистрирована", id));
            errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.toString(), "The required object was not found.",
                    String.format("Request with id=%s was not found", id), LocalDateTime.now().format(formatter));
        } else if (e.getObject() instanceof CompilationsEntity) {
            log.error(String.format("Подборка с id = %s не зарегистрирована!", id));
            errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.toString(), "The required object was not found.",
                    String.format("Compilation with id=%s was not found", id), LocalDateTime.now().format(formatter));
        }
        return errorResponse;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handle(final DataIntegrityViolationException e) {
        String message = e.getMessage();
        log.error("Ошибка уникальности!");
        return new ErrorResponse(HttpStatus.CONFLICT.toString(), "Integrity constraint has been violated.",
                message, LocalDateTime.now().format(formatter));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handle(final IncorrectEventStateException e) {
        log.error("Условия не соблюдены!");
        return new ErrorResponse(HttpStatus.FORBIDDEN.toString(), "For the requested operation the conditions are not met.",
                "Only pending or canceled events can be changed", LocalDateTime.now().format(formatter));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handle(final RequestException e) {
        String message = e.getMessage();
        String description = e.getDescription();
        log.error(message);
        return new ErrorResponse(HttpStatus.CONFLICT.toString(), message, description, LocalDateTime.now().format(formatter));
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handle(final ValidationException e) {
        log.error("Ошибка валидации для списка категорий!");
        return new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), "Ошибка валидации для списка категорий!",
                "Ошибка валидации для списка категорий!", LocalDateTime.now().format(formatter));
    }

    @ExceptionHandler(UnexpectedTypeException.class)

    public ResponseEntity<ErrorResponse> handleUnexpectedTypeException(UnexpectedTypeException e) {

        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), "Ошибка",
                "Ошибка", LocalDateTime.now().format(formatter));

        return ResponseEntity.badRequest().body(errorResponse);

    }
}
