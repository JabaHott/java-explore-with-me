package ru.practicum.service;

import org.springframework.stereotype.Service;
import ru.practicum.HitResponseDto;
import ru.practicum.model.Hit;
import ru.practicum.model.StatsModel;

import java.time.LocalDateTime;
import java.util.List;

@Service
public interface HitService {
    HitResponseDto add(Hit hit);

    List<StatsModel> viewStats(LocalDateTime start, LocalDateTime end, String[] uris, Boolean unique);
}
