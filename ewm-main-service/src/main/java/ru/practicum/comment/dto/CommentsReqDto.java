package ru.practicum.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentsReqDto {
    @NotNull
    @NotBlank
    @Size(min = 10, max = 3000)
    private String body;
}