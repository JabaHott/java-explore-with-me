package ru.practicum.service;

import ru.practicum.HitResponseDto;
import ru.practicum.model.Hit;
import ru.practicum.model.StatsModel;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface HitService {
    HitResponseDto add(Hit hit);

    List<StatsModel> viewStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);

    Long getViews(String uri);

    Map<Long, Long> getViewsMap(List<String> eventsId);
}
