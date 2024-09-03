package ru.practicum.mapper;

//@UtilityClass
public class StatsMapper {
//    private static final String FORMATTER = "yyyy-MM-dd HH:mm:ss";
//
//    public static Hit HitRequestDtoToHit(HitRequestDto HitRequestDto) {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMATTER);
//        return new Hit(
//                null,
//                HitRequestDto.getApp(),
//                HitRequestDto.getUri(),
//                HitRequestDto.getIp(),
//                HitRequestDto.getTimestamp()
//        );
//    }
//
//    public static HitResponseDto HitToHitResponseDto(Hit Hit) {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMATTER);
//        return new HitResponseDto(
//                Hit.getId(),
//                Hit.getApp(),
//                Hit.getUri(),
//                Hit.getIp(),
//                URLEncoder.encode(Hit.getTimestamp().format(formatter), StandardCharsets.UTF_8)
//        );
//    }
//
//    public static StatsDtoResponse statsViewToStatsDtoResponse(StatsView statsView) {
//        return new StatsDtoResponse(
//                statsView.getApp(),
//                statsView.getUri(),
//                statsView.getHits()
//        );
//    }
//
//    public static LocalDateTime asLocalDateTime(String timestamp) {
//        String decodedDate = URLDecoder.decode(timestamp, StandardCharsets.UTF_8);
//        return LocalDateTime.parse(decodedDate, DateTimeFormatter.ofPattern(FORMATTER));
//    }
}