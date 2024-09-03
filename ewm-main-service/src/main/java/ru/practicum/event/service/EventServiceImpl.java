package ru.practicum.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.service.CategoryService;
import ru.practicum.event.dto.EventRespDto;
import ru.practicum.event.dto.UpdateEventUserRequest;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.State;
import ru.practicum.event.model.StateAction;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.IncorrectEventStateException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.ValidationException;
import ru.practicum.location.dto.LocationDto;
import ru.practicum.location.mapper.LocationMapper;
import ru.practicum.location.model.Location;
import ru.practicum.location.repository.LocationRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;


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
        event.setViews(0);
        Location location = locationRepository.save(locationMapper.toLocation(locationDto));
        event.setLocation(location);
        if (event.getPaid() == null) {
            event.setPaid(false);
        }
        if (event.getParticipantLimit() == null) {
            event.setParticipantLimit(0);
        }
        if (event.getRequestModeration() == null) {
            event.setRequestModeration(true);
        }
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
            if (eventUpd.getStateAction() != null && eventUpd.getStateAction().equals(StateAction.CANCEL_REVIEW)) {
                event1.setState(State.CANCELED);
            } else {
                event1.setState(State.PUBLISHED);
            }
        } else {
            if (eventUpd.getStateAction() != null && eventUpd.getStateAction().equals(StateAction.SEND_TO_REVIEW)) {
                event1.setState(State.PUBLISHED);
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
        if (start == null || end == null) {
            return eventRepository.getAllEventsWithoutDate(users, states, cat, PageRequest.of(from, size));
        } else {
            return eventRepository.getAllEvents(users, states, cat, start,
                    end, PageRequest.of(from, size));
        }
    }

    @Override
    @Transactional
    public Event patchEventByAdmin(UpdateEventUserRequest eventUpd, Long eventId) {
        checkEvent(eventId);
        Event event = eventRepository.getReferenceById(eventId);
        checkEventState(event);
        event = patchingEvent(event, eventUpd);
        if (eventUpd.getStateAction() != null) {
            if (eventUpd.getStateAction().equals(StateAction.PUBLISH_EVENT) &&
                    (event.getState().equals(State.PENDING))) {
                Event updatedEvent = patchingEvent(event, eventUpd);
                updatedEvent.setState(State.PUBLISHED);
                event = eventRepository.save(updatedEvent);
            } else if (eventUpd.getStateAction().equals(StateAction.REJECT_EVENT) &&
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
            try {
                categories.forEach(categoryService::findCategoryById);
            } catch (NotFoundException e) {
                throw new ValidationException("ZDES BUDET TEXT");
            }
        }
        String formattedText = "%" + text.toLowerCase() + "%";
        if (start == null || end == null) {
            return eventRepository.getAllEventsWithSortWithoutDate(formattedText, categories,
                    paid, onlyAvailable, sort, PageRequest.of(from, size));
        } else {
            return eventRepository.getAllEventsWithSortWithDate(formattedText, categories, paid,
                    onlyAvailable, start, end, sort, PageRequest.of(from, size));
        }
    }

    @Override
    @Transactional
    public Event getEventsById(Long id, Integer views) {
        Event event = eventRepository.getReferenceById(id);
        checkEvent(id);
        event.setViews(views != null ? views : 0);
        return eventRepository.save(event);
    }

    @Override
    @Transactional
    public Event getEventsById(Long id) {
        checkEvent(id);
        return eventRepository.getReferenceById(id);
    }

    private Event patchingEvent(Event event, UpdateEventUserRequest eventUpd) {
        if (eventUpd.getAnnotation() != null) {
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
        if (eventUpd.getTitle() != null) {
            event.setTitle(eventUpd.getTitle());
        }
        if (eventUpd.getDescription() != null) {
            event.setDescription(eventUpd.getDescription());
        }
        return event;
    }

    private void checkUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            String error = String.format("Указанный пользователь " + userId + " не найден!");
            log.warn(error);
            throw new NotFoundException(userId, new User());
        }
    }

    private void checkEvent(Long eventId) {
        if (!eventRepository.existsById(eventId)) {
            String error = String.format("Указанное событие " + eventId + " не найдено!");
            log.warn(error);
            throw new NotFoundException(eventId, new Event());
        }
    }

    private void checkEventState(Event event) {
        if (event.getState().equals(State.PUBLISHED)) {
            String error = "Нельзя редактировать опубликованные сообщения";
            log.warn(error);
            throw new IncorrectEventStateException(error);
        }
    }

}
