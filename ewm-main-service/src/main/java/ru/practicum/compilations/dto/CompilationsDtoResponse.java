package ru.practicum.compilations.dto;

import lombok.Data;
import ru.practicum.event.dto.EventRespDto;

import java.util.List;

@Data
public class CompilationsDtoResponse {
    private List<EventRespDto> events;
    private Long id;
    private Boolean pinned;
    private String title;
}
