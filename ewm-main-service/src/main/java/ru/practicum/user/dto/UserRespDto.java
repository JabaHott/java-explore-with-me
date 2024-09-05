package ru.practicum.user.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRespDto {
    private Long id;
    private String email;
    private String name;
}

