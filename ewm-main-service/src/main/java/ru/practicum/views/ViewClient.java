package ru.practicum.views;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.HitRequestDto;
import ru.practicum.client.StatsClient;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ViewClient {

    private final StatsClient statsClient;

    public void addHit(HttpServletRequest request) {
        log.info("Views. Client: 'addHit' method called");
        HitRequestDto hitRequestDto = new HitRequestDto("${stats-service-server.url}", request.getRequestURI(), request.getRemoteAddr(),
                LocalDateTime.now());
        statsClient.addHit(hitRequestDto);
        log.info(" QQQQQQQQ {}", hitRequestDto);
    }

    public Integer getViews(HttpServletRequest request) throws UnsupportedEncodingException {
        ResponseEntity<Object> response = statsClient.getViews(request.getRequestURI());
        Integer views = null;
        if (response.getBody() instanceof Integer) {
            views = (Integer) response.getBody();
        } else if (response.getBody() instanceof Long) {
            views = (Integer) (response.getBody());
        }
        return views;
    }
}
