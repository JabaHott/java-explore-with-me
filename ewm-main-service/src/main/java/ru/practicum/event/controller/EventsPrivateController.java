package ru.practicum.event.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventRespDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventUserRequest;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.service.EventService;
import ru.practicum.request.dto.RequestsDtoResponse;
import ru.practicum.request.dto.RequestsDtoUpdate;
import ru.practicum.request.dto.RequestsDtoUpdateStatus;
import ru.practicum.request.mapper.RequestMapper;
import ru.practicum.request.service.RequestService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users/{userId}/events")
@Validated
@Slf4j
@RequiredArgsConstructor
public class EventsPrivateController {
    private final EventService eventsService;
    private final EventMapper eventsMapper;
    private final RequestMapper requestMapper;
    private final RequestService requestService;

    @PostMapping
    public ResponseEntity<EventRespDto> addEvent(
            @Positive @PathVariable Long userId,
            @Valid @RequestBody NewEventDto newEventDto
    ) {
        log.info("Events. Private Controller: 'addEvent' method called тело {}", newEventDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(eventsService.create(
                        eventsMapper.toEvent(newEventDto), userId, newEventDto.getCategory(), newEventDto.getLocation())
                );
    }

    @GetMapping
    public ResponseEntity<List<EventRespDto>> getAllEventsByUserId(
            @Positive @PathVariable Long userId,
            @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
            @Positive @RequestParam(defaultValue = "10") Integer size
    ) {
        log.info("Events. Private Controller: 'getAllEventsByUserId' method called");
        return ResponseEntity.ok(eventsService.getEventsByCreator(userId, from, size).stream()
                .map(eventsMapper::toEventRespDto)
                .collect(Collectors.toList()));
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventRespDto> getEventsByIdByUserId(
            @Positive @PathVariable Long userId,
            @Positive @PathVariable Long eventId
    ) {
        log.info("Events. Private Controller: 'getEventsByIdByUserId' method called");
        return ResponseEntity.ok(eventsMapper.toEventRespDto(
                eventsService.getEventByCreator(userId, eventId)
        ));
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventRespDto> patchEvent(
            @Positive @PathVariable Long userId,
            @Positive @PathVariable Long eventId,
            @Valid @RequestBody UpdateEventUserRequest updateEventUserRequest
    ) {
        log.info("Events. Private Controller: 'patchEvent' method called тело {}", updateEventUserRequest);
        return ResponseEntity.ok(eventsMapper.toEventRespDto(
                eventsService.patchEventByCreator(userId, updateEventUserRequest, eventId)
        ));
    }

    @GetMapping("/{eventId}/requests")
    public ResponseEntity<List<RequestsDtoResponse>> getAllRequestsByEventIdByUserId(
            @Positive @PathVariable Long userId,
            @Positive @PathVariable Long eventId
    ) {
        log.info("Events. Private Controller: 'getAllRequestsByEventIdByUserId' method called");
        return ResponseEntity.ok(
                requestService.getAllRequestsByEventIdByUserId(userId, eventId).stream()
                        .map(requestMapper::fromRequestEntityToRequestDtoResponse)
                        .collect(Collectors.toList())
        );
    }

    @PatchMapping("/{eventId}/requests")
    public ResponseEntity<RequestsDtoUpdateStatus> updateRequestsStatus(
            @Positive @PathVariable Long userId,
            @Positive @PathVariable Long eventId,
            @Valid @RequestBody RequestsDtoUpdate requestsDtoUpdate
    ) {
        log.info("Events. Private Controller: 'updateRequestsStatus' method called");
        return ResponseEntity.ok(
                requestService.updateRequestsStatus(userId, eventId, requestsDtoUpdate)
        );
    }
}