package ru.practicum.client;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Service;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class EventClient {

    private final StatsClient statsClient;

    public Map<Long, Integer> getViews(List<Long> eventsId) {
        ResponseEntity<Object> response = statsClient.getViewsMap(eventsId);
        Map<Long, Integer> viewsMap = new HashMap<>();
        if (response.getStatusCode().is2xxSuccessful()) {
            Object responseBody = response.getBody();
            if (responseBody instanceof Map) {
                Map<Long, Object> responseBodyMap = (Map<Long, Object>) responseBody;
                for (Map.Entry<Long, Object> entry : responseBodyMap.entrySet()) {
                    Long eventId = entry.getKey();
                    Integer views = Integer.parseInt(entry.getValue().toString());
                    viewsMap.put(eventId, views);
                }
            }
        }
        return viewsMap;
    }

}
