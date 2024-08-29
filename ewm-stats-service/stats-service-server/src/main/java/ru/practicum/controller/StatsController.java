package ru.practicum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.HitRequestDto;
import ru.practicum.HitResponseDto;
import ru.practicum.mapper.HitMapper;
import ru.practicum.model.Hit;
import ru.practicum.model.StatsModel;
import ru.practicum.service.HitService;

import java.time.LocalDateTime;
import java.util.List;


@RequiredArgsConstructor
@Slf4j
@RestController
public class StatsController {
    private final HitMapper hitMapper;
    private final HitService hitService;

    @PostMapping("/hit")
    public HitResponseDto add(@Valid @RequestBody HitRequestDto hitDto) {
        log.info("Получен Post запрос /hit с телом {}", hitDto);
        Hit hit = hitMapper.toHit(hitDto);
        return hitService.add(hit);
    }

    @GetMapping("/stats")
    public List<StatsModel> getStats(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                     @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                     @RequestParam(required = false) String[] uris,
                                     @RequestParam(defaultValue = "false") Boolean unique) {
        log.info("Получен get запрос /stats с телом {}, unique {}, start {}, end {}", uris, unique, start, end);
        return hitService.viewStats(start, end, uris, unique);
    }

}
