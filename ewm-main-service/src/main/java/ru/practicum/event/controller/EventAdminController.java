package ru.practicum.event.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventGetDto;
import ru.practicum.event.dto.PatchAdminRespDto;
import ru.practicum.event.dto.UpdateEventAdminRequest;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.State;
import ru.practicum.event.service.EventService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/events")
@Slf4j
@RequiredArgsConstructor
public class EventAdminController {
    private final EventService eventsService;
    private final EventMapper mapper;

    @GetMapping
    public ResponseEntity<List<EventGetDto>> getAllEvents(
            @RequestParam(required = false) List<Long> users,
            @RequestParam(required = false) List<State> states,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
            @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
            @Positive @RequestParam(defaultValue = "10") Integer size
    ) {
        log.info("Events. Admin Controller: 'getAllEvents' method called");
        return ResponseEntity.ok(
                eventsService.getEventAdmin(users, states, categories, rangeStart, rangeEnd, from, size).stream()
                        .map(mapper::toEventGetDto)
                        .collect(Collectors.toList())
        );
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<PatchAdminRespDto> patchEventStatus(
            @Positive @PathVariable Long eventId,
            @Valid @RequestBody UpdateEventAdminRequest eventUpd
    ) {
        log.info("Events. Admin Controller: 'patchEventStatus' method called");
        return ResponseEntity.ok(mapper.toPatchAdminRespDto(eventsService.patchEventByAdmin(eventUpd, eventId)));
    }
}