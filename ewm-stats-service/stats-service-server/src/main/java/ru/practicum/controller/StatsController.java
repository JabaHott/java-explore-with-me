package ru.practicum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.HitRequestDto;
import ru.practicum.HitResponseDto;
import ru.practicum.mapper.DateTimeMapper;
import ru.practicum.mapper.HitMapper;
import ru.practicum.model.Hit;
import ru.practicum.model.StatsModel;
import ru.practicum.service.HitService;

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
    public List<StatsModel> getStats(@RequestParam String start,
                                     @RequestParam String end,
                                     @RequestParam(required = false) String[] uris,
                                     @RequestParam(required = false, defaultValue = "false") Boolean unique) {
        log.info("Получен get запрос /stats с телом {}, unique {}, start {}, end {}", uris, unique, start, end);
        return hitService.viewStats(DateTimeMapper.toDate(start), DateTimeMapper.toDate(end), uris, unique);
    }

}
