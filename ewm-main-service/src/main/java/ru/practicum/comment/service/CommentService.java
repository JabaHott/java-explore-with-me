package ru.practicum.comment.service;

import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.comment.dto.CommentsReqDto;
import ru.practicum.comment.model.Comment;

public interface CommentService {
    @Transactional
    Comment addComment(Long userId, Long eventId, Comment comment);

    @Transactional
    void deleteComment(Long commentId);

    @Transactional
    Comment update(Long userId, Long commentId, CommentsReqDto commentsReqDto);

    @Transactional
    void deleteCommentByAuthor(Long userId, Long commentId);

    @Transactional(readOnly = true)
    Comment getCommentById(Long commentId);

    @Transactional(readOnly = true)
    Page<Comment> getAllCommentsByEventId(Long eventId, Integer from, Integer size);

    @Transactional
    Page<Comment> getAllCommentsByFilter(Long userId, Long eventId, String text, Integer from, Integer size);
}
