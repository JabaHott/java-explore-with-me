package ru.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.practicum.category.dto.CategoryRespDto;
import ru.practicum.event.model.State;
import ru.practicum.location.dto.LocationDto;
import ru.practicum.user.dto.UserReqDto;

import java.time.LocalDateTime;

@Data
public class EventGetDto {
    private Long Id;
    @NotNull
    private String title;
    @NotBlank
    private String annotation;
    @NotNull
    private CategoryRespDto category;
    @NotNull
    private Boolean paid;
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    @NotNull
    private UserReqDto initiator;
    private Integer views;
    private Integer confirmedRequests;
    private String description;
    private Integer participantLimit;
    private State state;
    @JsonFormat(pattern = "yyyy-mm-dd HH:mm:ss")
    private LocalDateTime createdOn;
    @JsonFormat(pattern = "yyyy-mm-dd HH:mm:ss")
    private LocalDateTime publishedOn;
    private LocationDto location;
    private Boolean requestModeration;
}
