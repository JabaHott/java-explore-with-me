package ru.practicum.comment.controller;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comment.dto.CommentsRespDto;
import ru.practicum.comment.mapper.CommentMapper;
import ru.practicum.comment.service.CommentService;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/comments")
@Validated
@Slf4j
@RequiredArgsConstructor
public class CommentAdminController {
    private final CommentService commentService;
    private final CommentMapper commentMapper;

    @GetMapping
    public ResponseEntity<List<CommentsRespDto>> getAllCommentsByFilter(
            @Positive @RequestParam(required = false) Long userId,
            @Positive @RequestParam(required = false) Long eventId,
            @RequestParam(required = false) String text,
            @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
            @Positive @RequestParam(defaultValue = "10") Integer size
    ) {
        log.info("Comments. Admin Controller: 'getAllCommentsByFilter' method called");
        return ResponseEntity.ok(
                commentService.getAllCommentsByFilter(userId, eventId, text, from, size).stream()
                        .map(commentMapper::toCommentRespDto)
                        .collect(Collectors.toList())
        );
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteCommentById(
            @Positive @PathVariable Long commentId
    ) {
        log.info("Comments. Admin Controller: 'deleteCommentById' method called");
        commentService.deleteComment(commentId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
