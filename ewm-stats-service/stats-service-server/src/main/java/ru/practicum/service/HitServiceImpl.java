package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.HitResponseDto;
import ru.practicum.mapper.HitMapper;
import ru.practicum.model.Hit;
import ru.practicum.model.StatsModel;
import ru.practicum.repository.HitRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

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
    public List<StatsModel> viewStats(LocalDateTime start, LocalDateTime end, String[] uris, Boolean unique) {
        List<String> uri;
        if (uris == null) {
            uri = null;
        } else {
            uri = Arrays.asList(uris);
        }
        return unique ?
                hitRepository.getUniqueStats(start, end, uri)
                : hitRepository.getStats(start, end, uri);
    }

}
