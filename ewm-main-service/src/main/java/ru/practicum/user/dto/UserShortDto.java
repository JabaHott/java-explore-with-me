package ru.practicum.user.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserShortDto {
    private Long id;
    @NotNull
    private String name;
}

