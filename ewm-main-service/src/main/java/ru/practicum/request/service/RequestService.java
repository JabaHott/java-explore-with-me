package ru.practicum.request.service;

import ru.practicum.request.dto.RequestsDtoUpdate;
import ru.practicum.request.dto.RequestsDtoUpdateStatus;
import ru.practicum.request.model.RequestEntity;

import java.util.List;

public interface RequestService {
    List<RequestEntity> getAllRequestsByUserId(Long userId);

    RequestEntity addRequest(Long userId, Long eventId);

    RequestEntity requestCancel(Long userId, Long requestId);

    List<RequestEntity> getAllRequestsByEventIdByUserId(Long userId, Long eventId);

    RequestsDtoUpdateStatus updateRequestsStatus(Long userId, Long eventId, RequestsDtoUpdate requestsDtoUpdate);
}