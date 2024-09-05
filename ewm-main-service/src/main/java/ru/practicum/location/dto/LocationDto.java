package ru.practicum.location.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LocationDto {
    @NotNull
    @Max(90)
    @Min(-90)
    private Double lat;
    @NotNull
    @Max(180)
    @Min(-180)
    private Double lon;
}
