package ru.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.practicum.event.model.StateAction;
import ru.practicum.location.dto.LocationDto;
import ru.practicum.valid.EventTwoHoursPastValid;

import java.time.LocalDateTime;

@Data
@EventTwoHoursPastValid
public class UpdateEventUserRequest {
    @Size(max = 2000, min = 20)
    private String annotation;
    @PositiveOrZero
    private Long category;
    @Size(max = 7000, min = 20)
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private LocationDto location;
    private Boolean paid;
    @PositiveOrZero
    private Integer participantLimit;
    private Boolean requestModeration;
    private StateAction stateAction;
    @Size(max = 120, min = 3)
    private String title;
}
