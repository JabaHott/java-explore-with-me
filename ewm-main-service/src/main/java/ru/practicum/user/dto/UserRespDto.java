package ru.practicum.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRespDto {
    Long id;
    @NotNull
    @Email
    String email;
    @NotNull
    String name;
}

