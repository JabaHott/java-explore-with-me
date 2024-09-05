package ru.practicum.request.controller;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.request.dto.RequestsDtoResponse;
import ru.practicum.request.mapper.RequestMapper;
import ru.practicum.request.service.RequestService;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/users/{userId}/requests")
@Slf4j
@RequiredArgsConstructor
@Validated
public class RequestController {
    private final RequestService requestService;
    private final RequestMapper requestMapper;


    @GetMapping
    public ResponseEntity<List<RequestsDtoResponse>> getAllRequestsByUserId(
            @Positive @PathVariable Long userId
    ) {
        log.info("Requests. Controller: 'getAllRequestsByUserId' method called");
        return ResponseEntity.ok(
                requestService.getAllRequestsByUserId(userId).stream()
                        .map(requestMapper::fromRequestViewToRequestDtoResponse)
                        .collect(Collectors.toList())
        );
    }

    @PostMapping
    public ResponseEntity<RequestsDtoResponse> addRequest(
            @Positive @PathVariable(name = "userId") Long userId,
            @Positive @RequestParam(name = "eventId") Long eventId
    ) {
        log.info("Requests. Controller: 'addRequest' method called, {}, {}", userId, eventId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        requestMapper.fromRequestEntityToRequestDtoResponse(requestService.addRequest(userId, eventId))
                );
    }

    @PatchMapping("/{requestId}/cancel")
    public ResponseEntity<RequestsDtoResponse> requestCancel(
            @Positive @PathVariable Long userId,
            @Positive @PathVariable Long requestId
    ) {
        log.info("Requests. Controller: 'requestCancel' method called");
        return ResponseEntity.ok(
                requestMapper.fromRequestEntityToRequestDtoResponse(requestService.requestCancel(userId, requestId))
        );
    }
}