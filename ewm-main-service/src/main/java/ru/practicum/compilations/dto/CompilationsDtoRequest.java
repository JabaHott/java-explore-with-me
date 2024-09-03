package ru.practicum.compilations.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class CompilationsDtoRequest {
    private List<Long> events;
    private Boolean pinned;
    @NotNull
    @NotBlank
    @Size(max = 50)
    private String title;
}