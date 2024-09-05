package ru.practicum.compilations.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class CompilationsDtoUpdate {
    private Set<Long> events;
    private Boolean pinned;
    @Size(max = 50)
    private String title;
}