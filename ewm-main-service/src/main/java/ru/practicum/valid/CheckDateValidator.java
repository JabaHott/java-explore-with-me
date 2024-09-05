package ru.practicum.valid;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.practicum.event.dto.NewEventDto;

import java.time.LocalDateTime;

public class CheckDateValidator implements ConstraintValidator<EventTwoHoursPastValid, NewEventDto> {
    @Override
    public void initialize(EventTwoHoursPastValid constraintAnnotation) {
    }

    @Override
    public boolean isValid(NewEventDto newEventDto, ConstraintValidatorContext constraintValidatorContext) {
        LocalDateTime eventDate = newEventDto.getEventDate();
        LocalDateTime currentPlusHours = LocalDateTime.now().plusHours(2);
        if (eventDate == null) {
            return false;
        }
        return currentPlusHours.isBefore(eventDate);
    }
}