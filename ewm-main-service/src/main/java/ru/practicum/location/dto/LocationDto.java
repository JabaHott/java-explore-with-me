package ru.practicum.location.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LocationDto {
    @NotNull
    private Double lat;
    @NotNull
    private Double lon;
}
