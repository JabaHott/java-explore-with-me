package ru.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.HitRequestDto;
import ru.practicum.HitResponseDto;
import ru.practicum.StatsDtoResponse;
import ru.practicum.model.Hit;
import ru.practicum.model.StatsModel;


import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class StatsMapper {
    private static final String FORMATTER = "yyyy-MM-dd HH:mm:ss";

    public static Hit hitDtoRequestToHitEntity(HitRequestDto hitDtoRequest) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMATTER);
        return new Hit(
                null,
                hitDtoRequest.getApp(),
                hitDtoRequest.getUri(),
                hitDtoRequest.getIp(),
                hitDtoRequest.getTimestamp()
        );
    }

    public static HitResponseDto hitEntityToHitDtoResponse(Hit hitEntity) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMATTER);
        return new HitResponseDto(
                hitEntity.getId(),
                hitEntity.getApp(),
                hitEntity.getUri(),
                hitEntity.getIp(),
                hitEntity.getTimestamp()
        );
    }

    public static StatsDtoResponse statsViewToStatsDtoResponse(StatsModel statsModel) {
        return new StatsDtoResponse(
                statsModel.getApp(),
                statsModel.getUri(),
                statsModel.getHits()
        );
    }

    public static LocalDateTime asLocalDateTime(String timestamp) {
        String decodedDate = URLDecoder.decode(timestamp, StandardCharsets.UTF_8);
        return LocalDateTime.parse(decodedDate, DateTimeFormatter.ofPattern(FORMATTER));
    }
}