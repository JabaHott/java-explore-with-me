package ru.practicum.model;

import lombok.*;


@Getter
@AllArgsConstructor
@Setter
public class StatsModel {
    private String app;
    private String uri;
    private Long hits;
}
