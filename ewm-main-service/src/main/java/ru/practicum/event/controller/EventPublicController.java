package ru.practicum.event.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventGetDto;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.service.EventService;
import ru.practicum.views.ViewClient;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/events")
@Validated
@Slf4j
@RequiredArgsConstructor
public class EventPublicController {
    private final EventService eventsService;
    private final EventMapper eventsMapper;
    private final ViewClient viewClient;

    @GetMapping
    public ResponseEntity<List<EventGetDto>> getAllEventsWithFiltration(
            @RequestParam(defaultValue = "") String text,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) Boolean paid,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
            @RequestParam(defaultValue = "false") Boolean onlyAvailable,
            @RequestParam(required = false) String sort,
            @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
            @Positive @RequestParam(defaultValue = "10") Integer size,
            HttpServletRequest request
    ) {
        log.info("Events. Public Controller: 'getAllEventsWithFiltration' method called");
        viewClient.addHit(request);
        return ResponseEntity.ok(
                eventsService.getAllEventsWithFiltration(text, categories, paid, rangeStart, rangeEnd, onlyAvailable,
                                sort, from, size).stream()
                        .map(eventsMapper::toEventGetDto)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventGetDto> getEventsById(
            @Positive @PathVariable Long id,
            HttpServletRequest request
    ) throws UnsupportedEncodingException {
        log.info("Events. Public Controller: 'getEventsById' method called");
        viewClient.addHit(request);
        Integer views = viewClient.getViews(request);
        EventGetDto eventGetDto = eventsMapper.toEventGetDto(eventsService.getEventsByIdPubl(id));
        eventGetDto.setViews(views);
        return ResponseEntity.ok(eventGetDto);
    }

}
