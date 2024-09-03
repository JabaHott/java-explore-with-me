package ru.practicum.category.controller;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.dto.CategoryRespDto;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.category.service.CategoryService;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/categories")
@Validated
@Slf4j
@RequiredArgsConstructor
public class CategoryPublicController {
    private final CategoryMapper mapper;
    private final CategoryService service;

    @GetMapping
    public ResponseEntity<List<CategoryRespDto>> getAll(@PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer from,
                                                        @PositiveOrZero @RequestParam(required = false, defaultValue = "10") Integer size) {
        log.info("Получен GET запрос всех категорий");
        return ResponseEntity.ok(service.findAllCategory(from, size).stream()
                .map(mapper::toCategoryRespDto)
                .collect(Collectors.toList()));
    }

    @GetMapping("/{catId}")
    public ResponseEntity<CategoryRespDto> getCategory(@PathVariable(name = "catId") Long catId) {
        log.info("Получен GET запрос категории {}", catId);
        return ResponseEntity.ok(mapper.toCategoryRespDto(
                service.findCategoryById(catId)
        ));
    }
}
