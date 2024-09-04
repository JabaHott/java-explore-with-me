package ru.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import ru.practicum.category.dto.CategoryRespDto;
import ru.practicum.event.model.State;
import ru.practicum.location.dto.LocationDto;
import ru.practicum.user.dto.UserReqDto;

import java.time.LocalDateTime;

@Data
public class EventRespDto {
    private Long id;
    private String title;
    private String annotation;
    private CategoryRespDto category;
    private Boolean paid;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private UserReqDto initiator;
    private String description;
    private Integer participantLimit;
    private State state;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;
    private LocationDto location;
    private Boolean requestModeration;
}
