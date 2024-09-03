package ru.practicum.user.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserShortDto {
    Long id;
    @NotNull
    String name;
}

