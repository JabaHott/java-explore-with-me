package ru.practicum.comment.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentsRespDto {
    private Long id;
    private String body;
    private String published;
    private Long userId;
    private Long eventId;
}