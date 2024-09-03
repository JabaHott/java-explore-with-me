package ru.practicum.category.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ru.practicum.category.dto.CategoryReqDto;
import ru.practicum.category.dto.CategoryRespDto;
import ru.practicum.category.model.Category;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper {

    Category toCategory(CategoryReqDto categoryReqDto);

    CategoryReqDto toCategoryReqDto(Category category);
    CategoryRespDto toCategoryRespDto(Category category);
}
