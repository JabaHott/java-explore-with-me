package ru.practicum.comment.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.HitRequestDto;
import ru.practicum.client.StatsClient;
import ru.practicum.comment.dto.CommentsRespDto;
import ru.practicum.comment.mapper.CommentMapper;
import ru.practicum.comment.service.CommentService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/comments")
@Validated
@Slf4j
@RequiredArgsConstructor
public class CommentsPublicController {

    private final CommentService commentService;
    private final CommentMapper commentMapper;
    private final StatsClient statsClient;

    @GetMapping("/{commentId}")
    public ResponseEntity<CommentsRespDto> getCommentById(
            @Positive @PathVariable Long commentId,
            HttpServletRequest request
    ) {
        log.info("Comments. Public Controller: 'getCommentById' method called");
        statsClient.addHit(new HitRequestDto("${stats-service-server.url}", request.getRequestURI(), request.getRemoteAddr(),
                LocalDateTime.now()));
        return ResponseEntity.ok(
                commentMapper.toCommentRespDto(commentService.getCommentById(commentId))
        );
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<CommentsRespDto>> getAllCommentsByEventId(
            @Positive @PathVariable Long eventId,
            @PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer from,
            @PositiveOrZero @RequestParam(required = false, defaultValue = "10") Integer size,
            HttpServletRequest request
    ) {
        log.info("Comments. Public Controller: 'getAllCommentsByEventId' method called");
        statsClient.addHit(new HitRequestDto("${stats-service-server.url}", request.getRequestURI(), request.getRemoteAddr(),
                LocalDateTime.now()));
        return ResponseEntity.ok(
                commentService.getAllCommentsByEventId(eventId, from, size).stream()
                        .map(commentMapper::toCommentRespDto)
                        .collect(Collectors.toList())
        );
    }
}