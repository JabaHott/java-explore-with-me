package ru.practicum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.HitRequestDto;
import ru.practicum.HitResponseDto;
import ru.practicum.mapper.StatsMapper;
import ru.practicum.model.Hit;
import ru.practicum.StatsDtoResponse;
import ru.practicum.service.HitService;

import java.time.LocalDateTime;
import java.util.ArrayList;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Slf4j
@RestController
public class StatsController {
    private final HitService hitService;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public HitResponseDto add(@Valid @RequestBody HitRequestDto hitDto) {
        log.info("Получен Post запрос /hit с телом {}", hitDto);
        Hit hit = StatsMapper.hitDtoRequestToHitEntity(hitDto);
        return hitService.add(hit);
    }

    @GetMapping("/stats")
    public List<StatsDtoResponse> getStats(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                           @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                           @RequestParam(required = false) List<String> uris,
                                           @RequestParam(defaultValue = "false") Boolean unique) {
        uris = (uris == null) ? new ArrayList<>() : uris;
        log.info("Получен get запрос /stats с телом {}, unique {}, start {}, end {}", uris, unique, start, end);
        return hitService.viewStats(start, end, uris, unique).stream()
                .map(StatsMapper::statsViewToStatsDtoResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/views")
    public Long getViews(@RequestParam(name = "uri") String uri) {
        log.info("Stats-service. Controller: 'getViews' method called");
        return hitService.getViews(uri);
    }

    @GetMapping("/views/map")
    public Map<Long, Long> getViews(@RequestParam(name = "uri") List<Long> eventsId) {
        log.info("Stats-service. Controller: 'getViews' method called");

        List<String> events = new ArrayList<>();
        for (Long i : eventsId) {
            String uri = "/events/" + i;
            events.add(uri);
        }
        return hitService.getViewsMap(events);
    }
}
