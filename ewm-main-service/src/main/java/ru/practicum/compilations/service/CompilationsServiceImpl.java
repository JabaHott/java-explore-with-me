package ru.practicum.compilations.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.compilations.dto.CompilationsDtoRequest;
import ru.practicum.compilations.dto.CompilationsDtoUpdate;
import ru.practicum.compilations.model.CompilationsEntity;
import ru.practicum.compilations.repository.CompilationsRepository;
import ru.practicum.event.model.Event;
import ru.practicum.event.service.EventService;
import ru.practicum.exception.NotFoundException;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CompilationsServiceImpl implements CompilationsService {
    private final CompilationsRepository compilationsRepository;
    private final EventService eventsService;

    @Override
    @Transactional
    public CompilationsEntity addCompilation(CompilationsDtoRequest compilationsDtoRequest) {
        log.info("Compilations. Service: 'addCompilation' method called");
        try {
            CompilationsEntity compilations = new CompilationsEntity();
            List<Event> events = new ArrayList<>();
            if (compilationsDtoRequest.getPinned() == null) {
                compilations.setPinned(false);
            } else {
                compilations.setPinned(compilationsDtoRequest.getPinned());
            }
            compilations.setTitle(compilationsDtoRequest.getTitle());
            if (compilationsDtoRequest.getEvents() == null) {
                compilations.setEvents(null);
            } else {
                compilationsDtoRequest.getEvents().forEach(item -> {
                    events.add(eventsService.getEventsById(item));
                });
            }
            compilations.setEvents(events);
            return compilationsRepository.save(compilations);
        } catch (DataAccessException e) {
            throw new DataIntegrityViolationException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public void deleteCompilation(Long compId) {
        log.info("Compilations. Service: 'deleteCompilation' method called");
        if (compilationsRepository.existsById(compId)) {
            compilationsRepository.deleteById(compId);
        } else {
            throw new NotFoundException(compId, new CompilationsEntity());
        }
    }

    @Override
    @Transactional
    public CompilationsEntity updateCompilation(Long compId, CompilationsDtoUpdate compilationsDtoUpdate) {
        log.info("Compilations. Service: 'updateCompilation' method called");
        CompilationsEntity compilations = compilationsRepository.findById(compId).orElseThrow(() ->
                new NotFoundException(compId, new CompilationsEntity()));
        if (compilationsDtoUpdate.getEvents() != null) {
            List<Event> eventsList = new ArrayList<>();
            compilationsDtoUpdate.getEvents().forEach(item -> {
                eventsList.add(eventsService.getEventsById(item));
            });
            compilations.setEvents(eventsList);
        }
        if (compilationsDtoUpdate.getPinned() != null) {
            compilations.setPinned(compilationsDtoUpdate.getPinned());
        }
        if (compilationsDtoUpdate.getTitle() != null) {
            compilations.setTitle(compilationsDtoUpdate.getTitle());
        }
        return compilationsRepository.save(compilations);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CompilationsEntity> getAllCompilations(Boolean pinned, Integer from, Integer size) {
        log.info("Compilations. Service: 'getAllCompilations' method called");
        return compilationsRepository.getAllCompilations(pinned, PageRequest.of(from, size));
    }

    @Override
    @Transactional(readOnly = true)
    public CompilationsEntity getCompilationById(Long compId) {
        log.info("Compilations. Service: 'getCompilationById' method called");
        return compilationsRepository.findById(compId).orElseThrow(() ->
                new NotFoundException(compId, new CompilationsEntity()));
    }
}