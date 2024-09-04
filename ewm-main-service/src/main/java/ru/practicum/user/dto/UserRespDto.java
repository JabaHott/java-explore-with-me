package ru.practicum.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRespDto {
    private Long id;
    @NotNull
    @Email
    private String email;
    @NotNull
    private String name;
}

