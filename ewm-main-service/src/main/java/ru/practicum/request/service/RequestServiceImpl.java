package ru.practicum.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.State;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.event.service.EventService;
import ru.practicum.exception.IncorrectEventStateException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.RequestException;
import ru.practicum.request.dto.RequestsDtoResponse;
import ru.practicum.request.dto.RequestsDtoUpdate;
import ru.practicum.request.dto.RequestsDtoUpdateStatus;
import ru.practicum.request.mapper.RequestMapper;
import ru.practicum.request.model.RequestEntity;
import ru.practicum.request.model.RequestStatus;
import ru.practicum.request.repository.RequestRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.service.UserService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final UserService userService;
    private final EventService eventsService;
    private final RequestMapper requestMapper;
    private final EventRepository eventsRepository;

    @Override
    @Transactional(readOnly = true)
    public List<RequestEntity> getAllRequestsByUserId(Long userId) {
        log.info("Requests. Service: 'getAllRequestsByUserId' method called");
        return requestRepository.findAllByUserId(userId);
    }

    @Override
    @Transactional
    public RequestEntity addRequest(Long userId, Long eventId) {
        log.info("Requests. Service: 'addRequest' method called");
        User user = userService.findUserById(userId);
        Event event = eventsService.getEventsById(eventId);
        if (event.getState().equals(State.PUBLISHED)) {
            if (!requestRepository.existsByEvent_IdAndRequester_Id(eventId, userId)) {
                if (!event.getInitiator().equals(user)) {
                    if (event.getState().equals(State.PUBLISHED)) {
                        if ((event.getParticipantLimit() == 0) ||
                                (requestRepository.getConfirmedRequest(RequestStatus.CONFIRMED, eventId)
                                        < event.getParticipantLimit())) {
                            RequestEntity request = new RequestEntity();
                            request.setCreated(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
                            request.setRequester(user);
                            request.setEvent(event);
                            if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
                                request.setStatus(RequestStatus.CONFIRMED);
                            } else {
                                request.setStatus(RequestStatus.PENDING);
                            }
                            return requestRepository.save(request);
                        } else {
                            throw new RequestException("The event has reached its limit of participation requests.",
                                    "The event has reached its limit of participation requests.");
                        }
                    } else {
                        throw new RequestException("You can't participate in an unpublished event.",
                                "You can't participate in an unpublished event.");
                    }
                } else {
                    throw new RequestException("An event initiator cannot add a request to their event.",
                            "An event initiator cannot add a request to their event.");
                }
            } else {
                throw new RequestException("You can't add a repeat request.", "You can't add a repeat request.");
            }
        } else {
            throw new DataIntegrityViolationException("Попытка добавить заявку к необупликованному событию!");
        }
    }

    @Override
    @Transactional
    public RequestEntity requestCancel(Long userId, Long requestId) {
        log.info("Requests. Service: 'requestCancel' method called");
        RequestEntity request = requestRepository.findByIdAndRequester_Id(requestId, userId);
        if (request != null) {
            request.setStatus(RequestStatus.CANCELED);
            return requestRepository.save(request);
        } else {
            throw new NotFoundException(requestId, RequestEntity.class);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<RequestEntity> getAllRequestsByEventIdByUserId(Long userId, Long eventId) {
        log.info("Requests. Service: 'getAllRequestsByEventIdByUserId' method called");
        return requestRepository.getAllRequestsByEventIdByUserId(eventId, userId);
    }

    @Override
    @Transactional
    public RequestsDtoUpdateStatus updateRequestsStatus(Long userId, Long eventId, RequestsDtoUpdate requestsDtoUpdate) {
        log.info("Requests. Service: 'updateRequestsStatus' method called");
        Event event = eventsService.getEventByCreator(userId, eventId);
        if (event != null) {
            RequestsDtoUpdateStatus updateStatus = new RequestsDtoUpdateStatus();
            if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
                return updateStatus;
            }

            List<RequestsDtoResponse> confirmedRequests = new ArrayList<>();
            List<RequestsDtoResponse> rejectedRequests = new ArrayList<>();

            for (Long requestId : requestsDtoUpdate.getRequestIds()) {
                RequestEntity request = requestRepository.findById(requestId)
                        .orElseThrow(() -> new NotFoundException(requestId, RequestEntity.class));

                if (request.getStatus() != RequestStatus.PENDING) {
                    throw new DataIntegrityViolationException("Попытка обновить событие с окончательным статусом!");
                }

                if (RequestStatus.CONFIRMED.equals(requestsDtoUpdate.getStatus())) {
                    handleConfirmedRequest(event, request, confirmedRequests);
                } else if (RequestStatus.REJECTED.equals(requestsDtoUpdate.getStatus())) {
                    handleRejectedRequest(request, rejectedRequests);
                } else {
                    throw new IncorrectEventStateException("ZDES BUDET TEXT");
                }
            }

            updateStatus.setConfirmedRequests(confirmedRequests);
            updateStatus.setRejectedRequests(rejectedRequests);
            return updateStatus;
        } else {
            throw new NotFoundException(eventId, Event.class);
        }
    }

    private void handleConfirmedRequest(Event event, RequestEntity request, List<RequestsDtoResponse> confirmedRequests) {
        if (requestRepository.getConfirmedRequest(RequestStatus.CONFIRMED, event.getId()) >= event.getParticipantLimit()) {
            throw new RequestException("The event has reached its limit of participation requests.",
                    "The participant limit has been reached");
        }

        request.setStatus(RequestStatus.CONFIRMED);

        requestRepository.save(request);
        eventsRepository.save(event);

        confirmedRequests.add(requestMapper.fromRequestEntityToRequestDtoResponse(request));
    }

    private void handleRejectedRequest(RequestEntity request, List<RequestsDtoResponse> rejectedRequests) {
        request.setStatus(RequestStatus.REJECTED);
        requestRepository.save(request);
        rejectedRequests.add(requestMapper.fromRequestEntityToRequestDtoResponse(request));
    }
}