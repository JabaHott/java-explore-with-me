package ru.practicum.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.service.CategoryService;
import ru.practicum.client.EventClient;
import ru.practicum.event.dto.EventRespDto;
import ru.practicum.event.dto.UpdateEventAdminRequest;
import ru.practicum.event.dto.UpdateEventRequest;
import ru.practicum.event.dto.UpdateEventUserRequest;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.State;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.IncorrectEventStateException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.ValidationException;
import ru.practicum.location.dto.LocationDto;
import ru.practicum.location.mapper.LocationMapper;
import ru.practicum.location.model.Location;
import ru.practicum.location.repository.LocationRepository;
import ru.practicum.request.model.RequestEntity;
import ru.practicum.request.model.RequestStatus;
import ru.practicum.request.repository.RequestRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class EventServiceImpl implements EventService {
    private final EventMapper mapper;
    private final EventRepository eventRepository;
    private final LocationRepository locationRepository;
    private final UserRepository userRepository;
    private final CategoryService categoryService;
    private final LocationMapper locationMapper;
    private final RequestRepository requestRepository;
    private final EventClient client;

    @Override
    @Transactional
    public EventRespDto create(Event event, Long userId, Long catId, LocationDto locationDto) {
        checkUser(userId);
        event.setCategory(categoryService.findCategoryById(catId));
        event.setInitiator(userRepository.getReferenceById(userId));
        event.setConfirmedRequests(0);

        if (event.getCreatedOn() == null) {
            event.setCreatedOn(LocalDateTime.now());
        }
        if (event.getPublishedOn() == null) {
            event.setPublishedOn(LocalDateTime.now());
        }
        if (event.getEventDate() == null) {
            event.setEventDate(LocalDateTime.now());
        }
        event.setState(State.PENDING);
        Location location = locationRepository.save(locationMapper.toLocation(locationDto));
        event.setLocation(location);
        if (event.getPaid() == null) {
            event.setPaid(false);
        }
        if (event.getParticipantLimit() == null) {
            event.setParticipantLimit(0);
        }
        event.setViews(0);
        return mapper.toEventRespDto(eventRepository.save(event));
    }

    @Override
    @Transactional
    public Page<Event> getEventsByCreator(Long userId, Integer from, Integer size) {
        checkUser(userId);
        return eventRepository.findEventsByInitiatorId(userId, PageRequest.of(from, size));
    }

    @Override
    @Transactional
    public Event getEventByCreator(Long userId, Long eventId) {
        checkUser(userId);
        checkEvent(eventId);
        return eventRepository.findByIdAndInitiator_Id(eventId, userId);
    }

    @Override
    @Transactional
    public Event patchEventByCreator(Long userId, UpdateEventUserRequest eventUpd, Long eventId) {
        checkUser(userId);
        checkEvent(eventId);
        Event event = eventRepository.getReferenceById(eventId);
        checkEventState(event);
        Event event1 = patchingEvent(event, eventUpd);
        if (event1.getState().equals(State.PENDING)) {
            if (eventUpd.getStateAction() != null && eventUpd.getStateAction().equals(UpdateEventUserRequest.StateAction.CANCEL_REVIEW)) {
                event1.setState(State.CANCELED);
            } else {
                event1.setState(State.PUBLISHED);
            }
        } else {
            if (eventUpd.getStateAction() != null && eventUpd.getStateAction().equals(UpdateEventUserRequest.StateAction.SEND_TO_REVIEW)) {
                event1.setState(State.PENDING);
            } else {
                throw new IncorrectEventStateException("ZDES BUDET TEXT");
            }
        }
        return eventRepository.save(event1);
    }

    @Override
    @Transactional
    public Page<Event> getEventAdmin(List<Long> users, List<State> states, List<Long> cat,
                                     LocalDateTime start, LocalDateTime end,
                                     Integer from, Integer size) {
        Page<Event> page = null;
        if (start == null || end == null) {
            page = eventRepository.getAllEventsWithoutDate(users, states, cat, PageRequest.of(from, size));
        } else {
            page = eventRepository.getAllEvents(users, states, cat, start,
                    end, PageRequest.of(from, size));
        }
        return fillViewsCr(page);
    }

    @Override
    @Transactional
    public Event patchEventByAdmin(UpdateEventAdminRequest eventUpd, Long eventId) {
        checkEvent(eventId);
        Event event = eventRepository.getReferenceById(eventId);
        checkEventState(event);
        event = patchingEvent(event, eventUpd);
        if (eventUpd.getStateAction() != null) {
            if (eventUpd.getStateAction().equals(UpdateEventAdminRequest.StateAction.PUBLISH_EVENT) &&
                    (event.getState().equals(State.PENDING))) {
                Event updatedEvent = patchingEvent(event, eventUpd);
                updatedEvent.setState(State.PUBLISHED);
                event = eventRepository.save(updatedEvent);
            } else if (eventUpd.getStateAction().equals(UpdateEventAdminRequest.StateAction.REJECT_EVENT) &&
                    event.getState().equals(State.PENDING)) {
                Event updatedEvent = patchingEvent(event, eventUpd);
                updatedEvent.setState(State.CANCELED);
                event = eventRepository.save(updatedEvent);
            } else {
                throw new DataIntegrityViolationException("");
            }
        }
        return event;
    }

    @Override
    @Transactional
    public Page<Event> getAllEventsWithFiltration(
            String text, List<Long> categories, Boolean paid, LocalDateTime start, LocalDateTime end, Boolean onlyAvailable,
            String sort, Integer from, Integer size
    ) {
        log.info("Events. Service: 'getAllEventsWithFiltration' method called");
        if (categories != null) {
            Long count = categoryService.countByIdIn(categories);
            if (count != categories.size()) {
                throw new ValidationException("ZDES BUDET TEXT");
            }
        }
//        if (categories != null) {
//            try {
//                categories.forEach(categoryService::findCategoryById);
//            } catch (NotFoundException e) {
//                throw new ValidationException("ZDES BUDET TEXT");
//            }
//        }
        Page<Event> page = null;
        String formattedText = "%" + text.toLowerCase() + "%";
        if (start == null || end == null) {
            page = eventRepository.getAllEventsWithSortWithoutDate(formattedText, categories,
                    paid, PageRequest.of(from, size));
        } else {
            page = eventRepository.getAllEventsWithSortWithDate(formattedText, categories, paid,
                    start, end, PageRequest.of(from, size));
        }
        fillViewsCr(page);
        if (onlyAvailable) {
            page = filterByAvailability(page);
        }
        if (sort != null) {
            if (sort.equals("VIEWS")) {
                page = sortByViews(page);
            }
        }
        return page;
    }

    @Override
    @Transactional
    public Event getEventsByIdPubl(Long id) {
        Event event = eventRepository.getReferenceById(id);
        if (event.getState().equals(State.PUBLISHED)) {
            checkEvent(id);
        } else {
            throw new NotFoundException(id, Event.class);
        }
        return eventRepository.save(event);
    }

    @Override
    @Transactional
    public Event getEventsById(Long id) {
        checkEvent(id);
        return eventRepository.getReferenceById(id);
    }

    @Override
    public Set<Event> getEventsByIdIn(Set<Long> eventIds) {
        return eventRepository.findByIdIn(eventIds);
    }


    private Event patchingEvent(Event event, UpdateEventRequest eventUpd) {
        if (eventUpd.getAnnotation() != null && !eventUpd.getAnnotation().isBlank()) {
            event.setAnnotation(eventUpd.getAnnotation());
        }
        if (eventUpd.getCategory() != null) {
            categoryService.findCategoryById(eventUpd.getCategory());
            event.setEventDate(eventUpd.getEventDate());
        }
        if (eventUpd.getEventDate() != null) {
            event.setEventDate(eventUpd.getEventDate());
        }
        if (eventUpd.getLocation() != null) {
            event.setLocation(locationRepository.save(locationMapper.toLocation(eventUpd.getLocation())));
        }
        if (eventUpd.getPaid() != null) {
            event.setPaid(eventUpd.getPaid());
        }
        if (eventUpd.getParticipantLimit() != null) {
            event.setParticipantLimit(eventUpd.getParticipantLimit());
        }
        if (eventUpd.getRequestModeration() != null) {
            event.setRequestModeration(eventUpd.getRequestModeration());
        }
        if (eventUpd.getTitle() != null && !eventUpd.getTitle().isBlank()) {
            event.setTitle(eventUpd.getTitle());
        }
        if (eventUpd.getDescription() != null && !eventUpd.getDescription().isBlank()) {
            event.setDescription(eventUpd.getDescription());
        }
        return event;
    }

    private void checkUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            String error = String.format("Указанный пользователь " + userId + " не найден!");
            log.warn(error);
            throw new NotFoundException(userId, User.class);
        }
    }

    private void checkEvent(Long eventId) {
        if (!eventRepository.existsById(eventId)) {
            String error = String.format("Указанное событие " + eventId + " не найдено!");
            log.warn(error);
            throw new NotFoundException(eventId, Event.class);
        }
    }

    private void checkEventState(Event event) {
        if (event.getState().equals(State.PUBLISHED)) {
            String error = "Нельзя редактировать опубликованные сообщения";
            log.warn(error);
            throw new IncorrectEventStateException(error);
        }
    }

    public Page<Event> sortByViews(Page<Event> page) {
        return new PageImpl<>(page.stream()
                .sorted((e1, e2) -> e2.getViews().compareTo(e1.getViews()))
                .collect(Collectors.toList()));

    }


    public Page<Event> filterByAvailability(Page<Event> page) {
        return new PageImpl<>(page.stream()
                .filter(event -> event.getParticipantLimit() > event.getConfirmedRequests())
                .collect(Collectors.toList()));
    }

    private Page<Event> fillViewsCr(Page<Event> page) {
        List<Long> eventsId = new ArrayList<>();
        for (Event e : page) {
            eventsId.add(e.getId());
        }
        List<RequestEntity> confirmedRequests = requestRepository.getConfirmedRequestsByEventIds(eventsId, RequestStatus.CONFIRMED);
        Map<Long, Integer> confirmedRequestsMap = confirmedRequests.stream()
                .collect(Collectors.groupingBy(r -> r.getEvent().getId(), Collectors.summingInt(r -> 1)));
        Map<Long, Integer> views = client.getViews(eventsId);

        for (Event e : page) {
            e.setConfirmedRequests(confirmedRequestsMap.getOrDefault(e.getId(), 0));
            e.setViews(views.get(e.getId())); // заполняем views
        }
        return page;
    }
}
