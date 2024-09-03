package ru.practicum.event.service;

import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.event.dto.EventRespDto;
import ru.practicum.event.dto.UpdateEventUserRequest;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.State;
import ru.practicum.location.dto.LocationDto;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    @Transactional
    EventRespDto create(Event event, Long userId, Long catId, LocationDto locationDto);

    @Transactional
    Page<Event> getEventsByCreator(Long userId, Integer from, Integer size);

    @Transactional
    Event getEventByCreator(Long userId, Long eventId);

    @Transactional
    Event patchEventByCreator(Long userId, UpdateEventUserRequest eventUpd, Long eventId);

    @Transactional
    Page<Event> getEventAdmin(List<Long> users, List<State> states, List<Long> cat,
                              LocalDateTime start, LocalDateTime end,
                              Integer from, Integer size);

    @Transactional
    Event patchEventByAdmin(UpdateEventUserRequest eventUpd, Long eventId);

    @Transactional
    Page<Event> getAllEventsWithFiltration(
            String text, List<Long> categories, Boolean paid, LocalDateTime start, LocalDateTime end, Boolean onlyAvailable,
            String sort, Integer from, Integer size
    );

    @Transactional
    Event getEventsById(Long id, Integer views);

    @Transactional
    Event getEventsById(Long eventId);
}
