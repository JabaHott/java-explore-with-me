package ru.practicum.mapper;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeMapper {
    public static LocalDateTime toDate(String timestamp) {

        return LocalDateTime.parse(URLDecoder.decode(timestamp, StandardCharsets.UTF_8), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
