package ru.practicum.comment.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comment.dto.CommentsReqDto;
import ru.practicum.comment.dto.CommentsRespDto;
import ru.practicum.comment.mapper.CommentMapper;
import ru.practicum.comment.service.CommentService;

@Controller
@RequestMapping("/users/{userId}/comments")
@Validated
@Slf4j
@RequiredArgsConstructor
public class CommentPrivateController {

    private final CommentService commentService;
    private final CommentMapper commentMapper;

    @PostMapping("/events/{eventId}")
    public ResponseEntity<CommentsRespDto> createComment(
            @Positive @PathVariable Long userId,
            @Positive @PathVariable Long eventId,
            @Valid @RequestBody CommentsReqDto commentsRequestDto
    ) {
        log.info("Comments. Private Controller: 'createComment' method called");
        return ResponseEntity.status(HttpStatus.CREATED).body(
                commentMapper.toCommentRespDto(
                        commentService.addComment(
                                userId, eventId, commentMapper.toComment(commentsRequestDto)
                        )
                )
        );
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentsRespDto> updateComment(
            @Positive @PathVariable Long userId,
            @Positive @PathVariable Long commentId,
            @Valid @RequestBody CommentsReqDto commentsRequestDto
    ) {
        log.info("Comments. Private Controller: 'updateComment' method called");
        return ResponseEntity.ok(
                commentMapper.toCommentRespDto(
                        commentService.update(userId, commentId, commentsRequestDto)
                )
        );
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteCommentByOwner(
            @Positive @PathVariable Long userId,
            @Positive @PathVariable Long commentId
    ) {
        log.info("Comments. Private Controller: 'deleteCommentByOwner' method called");
        commentService.deleteCommentByAuthor(userId, commentId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}