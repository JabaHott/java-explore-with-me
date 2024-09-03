package ru.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.*;
import lombok.Data;
import ru.practicum.LocalDateTimeDeserializer;
import ru.practicum.location.dto.LocationDto;
import ru.practicum.valid.EventTwoHoursPastValid;

import java.time.LocalDateTime;

@Data
@EventTwoHoursPastValid
public class NewEventDto {
    @NotNull
    @NotBlank
    @Size(max = 2000, min = 20)
    private String annotation;
    @NotNull
    @PositiveOrZero
    private Long category;
    @NotNull
    @NotBlank
    @Size(max = 10000, min = 20)
    private String description;
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @Future
    private LocalDateTime eventDate;
    @NotNull
    private LocationDto location;
    private Boolean paid;
    @PositiveOrZero
    private Integer participantLimit;
    private Boolean requestModeration;
    @NotNull
    @NotBlank
    @Size(max = 120, min = 3)
    private String title;
}
