package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.HitResponseDto;
import ru.practicum.exception.IncorrectDateException;
import ru.practicum.mapper.HitMapper;
import ru.practicum.model.Hit;
import ru.practicum.model.StatsModel;
import ru.practicum.repository.HitRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class HitServiceImpl implements HitService {

    private final HitRepository hitRepository;
    private final HitMapper mapper;

    @Override
    @Transactional
    public HitResponseDto add(Hit hit) {
        try {
            log.debug("Создан запись с телом {}", hit);
            return mapper.toHitResponseDto(hitRepository.save(hit));
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при сохранении в БД");
        }
    }

    @Override
    public List<StatsModel> viewStats(LocalDateTime start, LocalDateTime end, List<String> uri, Boolean unique) {
        if (start.isAfter(end)) {
            throw new IncorrectDateException("Дата начала интервала поиска не может быть позднее даты окончания интервала поиска");
        }
        List<StatsModel> stats = new ArrayList<>();
        if (unique) {
            log.info("start {}, end, {}, uri {}", start, end, uri);log.info("ветка 1{}", stats);
            return hitRepository.getUniqueStats(start, end, uri);
        } else {
            log.info("start {}, end, {}, uri {}", start, end, uri);
            log.info("Ветка 2 {}", stats);
            return hitRepository.getStats(start, end, uri);
        }
    }

    @Override
    public Long getViews(String uri) {
        log.info("Stats-service. Service: 'getViews' method called");
        return hitRepository.getViews(uri);
    }

    @Override
    public Map<Long, Long> getViewsMap(List<String> eventsId) {
        List<Object[]> results = hitRepository.countDistinctIpsByEventsId(eventsId);
        Map<Long, Long> resultMap = new HashMap<>();
        for (Object[] row : results) {
            String uri = (String) row[0];
            Long eventId = Long.valueOf(uri.substring(uri.lastIndexOf('/'), uri.length() - 1));
            Long count = (Long) row[1];
            resultMap.put(eventId, count);
        }
        return resultMap;
    }
}
