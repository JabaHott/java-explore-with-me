package ru.practicum.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.dto.CategoryReqDto;
import ru.practicum.category.dto.CategoryRespDto;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.category.service.CategoryService;

@RestController
@RequestMapping("/admin/categories")
@Validated
@Slf4j
@RequiredArgsConstructor
public class CategoryAdminController {
    private final CategoryMapper mapper;
    private final CategoryService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryRespDto createCategory(@RequestBody CategoryReqDto categoryReqDto) {
        log.info("Получен POST запрос с телом {}", categoryReqDto);
        return mapper.toCategoryRespDto(service.create(mapper.toCategory(categoryReqDto)));
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long catId) {
        log.info("Получен DELETE запрос с телом {}", catId);
        service.delete(catId);
    }

    @PatchMapping("/{catId}")
    public CategoryRespDto patchCategory(@RequestBody CategoryReqDto categoryReqDto,
                                         @PathVariable Long catId) {
        log.info("Получен PATCH запрос с телом {}", catId);
        return mapper.toCategoryRespDto(service.update(categoryReqDto, catId));
    }
}
