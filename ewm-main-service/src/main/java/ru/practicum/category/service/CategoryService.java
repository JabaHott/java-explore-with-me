package ru.practicum.category.service;

import org.springframework.data.domain.Page;
import ru.practicum.category.dto.CategoryReqDto;
import ru.practicum.category.model.Category;

public interface CategoryService {

    Category create(Category category);

    void delete(Long categoryId);

    Category update(CategoryReqDto categoryReqDto, Long catId);

    Page<Category> findAllCategory(Integer from, Integer size);

    Category findCategoryById(Long catId);
}
