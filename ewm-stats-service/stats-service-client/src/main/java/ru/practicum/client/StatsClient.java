package ru.practicum.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;

import ru.practicum.HitRequestDto;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;


@Service
public class StatsClient extends BaseClient {
    public static final String ADD_HIT_PREFIX = "/hit";
    public static final String GET_STATS_PREFIX = "/stats";
    public static final String GET_VIEWS_PREFIX = "/views";
    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    public StatsClient(@Value("${stats-service-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> addHit(HitRequestDto hitRequestDto) {
        return post(ADD_HIT_PREFIX, hitRequestDto);
    }

    public ResponseEntity<Object> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) throws UnsupportedEncodingException {
        String encodedStart = start != null ? URLEncoder.encode(start.format(formatter), StandardCharsets.UTF_8) : null;
        String encodedEnd = end != null ? URLEncoder.encode(end.format(formatter), StandardCharsets.UTF_8) : null;

        String urisString = String.join(",", uris);

        Map<String, Object> param = Map.of(
                "start", encodedStart,
                "end", encodedEnd,
                "uris", urisString,
                "unique", unique
        );
        return get(GET_STATS_PREFIX, param);
    }

    public ResponseEntity<Object> getViews(String uri) throws UnsupportedEncodingException {
        Map<String, Object> param = Map.of(
                "uri", uri
        );
        return get(GET_VIEWS_PREFIX + "?uri={uri}", param);
    }

    public ResponseEntity<Object> getViewsMap(List<Long> eventsId) {
        Map<String, Object> param = Map.of(
                "uri", eventsId
        );
        return get("/views/map" + "?uri={uri}", param);
    }

}