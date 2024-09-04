package ru.practicum.compilations.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.HitRequestDto;
import ru.practicum.client.StatsClient;
import ru.practicum.compilations.dto.CompilationsDtoResponse;
import ru.practicum.compilations.mapper.CompilationsMapper;
import ru.practicum.compilations.service.CompilationsService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/compilations")
@Validated
@Slf4j
@RequiredArgsConstructor
public class CompilationsPublicController {
    private final CompilationsService compilationService;
    private final CompilationsMapper compilationsMapper;
    private final StatsClient statsClient;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    @GetMapping
    public ResponseEntity<List<CompilationsDtoResponse>> getAllCompilations(
            @RequestParam(required = false) Boolean pinned,
            @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
            @Positive @RequestParam(defaultValue = "10") Integer size,
            HttpServletRequest request
    ) {
        log.info("Compilations. Public Controller: 'getAllCompilations' method called");
        statsClient.addHit(new HitRequestDto("${app-constant-name}", request.getRequestURI(), request.getRemoteAddr(),
                LocalDateTime.now()));
        return ResponseEntity.ok(
                compilationService.getAllCompilations(pinned, from, size).stream()
                        .map(compilationsMapper::fromCompilationsEntityToCompilationsDtoResponse)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/{compId}")
    public ResponseEntity<CompilationsDtoResponse> getCompilationById(
            @Positive @PathVariable Long compId,
            HttpServletRequest request
    ) {
        log.info("Compilations. Public Controller: 'getCompilationById' method called");
        statsClient.addHit(new HitRequestDto("${app-constant-name}", request.getRequestURI(), request.getRemoteAddr(),
                LocalDateTime.now()));
        return ResponseEntity.ok(
                compilationsMapper.fromCompilationsEntityToCompilationsDtoResponse(compilationService.getCompilationById(compId))
        );
    }
}