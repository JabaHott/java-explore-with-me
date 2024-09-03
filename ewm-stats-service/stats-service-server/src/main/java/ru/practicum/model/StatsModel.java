package ru.practicum.model;

import lombok.*;


@Getter
@AllArgsConstructor
public class StatsModel {
    private String app;
    private String uri;
    private Long hits;
}
