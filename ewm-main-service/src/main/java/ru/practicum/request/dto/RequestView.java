package ru.practicum.request.dto;

import ru.practicum.request.model.RequestStatus;

import java.time.LocalDateTime;

public class RequestView {
    private Long id;
    private LocalDateTime created;
    private Long event;
    private Long requester;
    private RequestStatus status;
}