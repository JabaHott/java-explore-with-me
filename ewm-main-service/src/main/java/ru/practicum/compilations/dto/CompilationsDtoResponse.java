package ru.practicum.compilations.dto;

import lombok.Data;
import ru.practicum.event.dto.EventRespDto;

import java.util.Set;

@Data
public class CompilationsDtoResponse {
    private Set<EventRespDto> events;
    private Long id;
    private Boolean pinned;
    private String title;
}
