package ru.practicum.event.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.HitRequestDto;
import ru.practicum.client.StatsClient;
import ru.practicum.event.dto.EventGetDto;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.service.EventService;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/events")
@Validated
@Slf4j
@RequiredArgsConstructor
public class EventPublicController {
    private final EventService eventsService;
    private final EventMapper eventsMapper;
    private final StatsClient statsClient;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


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
            @PositiveOrZero @RequestParam(defaultValue = "10") Integer size,
            HttpServletRequest request
    ) {
        log.info("Events. Public Controller: 'getAllEventsWithFiltration' method called");
        statsClient.addHit(new HitRequestDto("http://stats-server:9090", request.getRequestURI(), request.getRemoteAddr(),
                LocalDateTime.now()));
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
        String requestURI = request.getRequestURI();
        statsClient.addHit(new HitRequestDto("http://stats-server:9090",
                requestURI,
                request.getRemoteAddr(),
                LocalDateTime.now()));
        ResponseEntity<Object> response = statsClient.getViews(requestURI);
        Integer views = null;
        if (response.getBody() instanceof Integer) {
            views = (Integer) response.getBody();
        } else if (response.getBody() instanceof Long) {
            views = (Integer) (response.getBody());
        }
        return ResponseEntity.ok(eventsMapper.toEventGetDto(eventsService.getEventsById(id, views)));
    }

}
