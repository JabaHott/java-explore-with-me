package ru.practicum.compilations.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class CompilationsDtoRequest {
    private Set<Long> events;
    private boolean pinned;
    @NotBlank
    @Size(max = 50)
    private String title;
}