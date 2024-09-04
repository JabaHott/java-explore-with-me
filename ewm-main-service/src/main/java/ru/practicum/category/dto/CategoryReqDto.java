package ru.practicum.category.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CategoryReqDto {
    @Size(max = 50)
    @NotBlank
    private String name;
}
