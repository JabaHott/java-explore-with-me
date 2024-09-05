package ru.practicum.comment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.comment.dto.CommentsReqDto;
import ru.practicum.comment.model.Comment;
import ru.practicum.comment.repository.CommentRepository;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.IncorrectEventStateException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.time.LocalDateTime;

import static ru.practicum.event.model.State.PUBLISHED;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public Comment addComment(Long userId, Long eventId, Comment comment) {
        checkUser(userId);
        checkEvent(eventId);
        Event event = eventRepository.getReferenceById(eventId);
        User user = userRepository.getReferenceById(userId);
        if (event.getInitiator().equals(user)) {
            throw new DataIntegrityViolationException("Автор события не может оставлять комментарии под своими событиями!");
        }
        comment.setPublished(LocalDateTime.now());
        comment.setAuthor(user);
        comment.setEvent(event);
        return commentRepository.save(comment);
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId) {
        checkComment(commentId);
        commentRepository.deleteById(commentId);
    }

    @Override
    @Transactional
    public Comment update(Long userId, Long commentId, CommentsReqDto commentsReqDto) {
        checkUser(userId);
        checkComment(commentId);
        Comment comment = commentRepository.getReferenceById(commentId);
        if (userId == comment.getAuthor().getId()) {
            comment.setBody(commentsReqDto.getBody());
            return commentRepository.save(comment);
        } else {
            throw new DataIntegrityViolationException("Редактируемый комметарий оставлен другим пользователем");
        }
    }

    @Override
    @Transactional
    public void deleteCommentByAuthor(Long userId, Long commentId) {
        checkUser(userId);
        checkComment(commentId);
        Comment comment = commentRepository.getReferenceById(commentId);
        if (userId == comment.getAuthor().getId()) {
            commentRepository.deleteById(commentId);
        } else {
            throw new DataIntegrityViolationException("Редактируемый комметарий оставлен другим пользователем");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Comment getCommentById(Long commentId) {
        checkComment(commentId);
        log.info("Comments. Service: 'getCommentById' method called");
        return commentRepository.getReferenceById(commentId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Comment> getAllCommentsByEventId(Long eventId, Integer from, Integer size) {
        log.info("Comments. Service: 'getAllCommentsByEventId' method called");
        checkEvent(eventId);
        return commentRepository.findAllByEvent_Id(eventId, PageRequest.of(from, size));
    }

    @Override
    @Transactional
    public Page<Comment> getAllCommentsByFilter(Long userId, Long eventId, String text, Integer from, Integer size) {
        log.info("Comments. Service: 'getAllCommentsByFilter' method called {}, {}, {}", userId, eventId, text);
        return commentRepository.findAllCommentsWithFilterForAdmin(userId, eventId, text, PageRequest.of(from, size));
    }

    private void checkUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            log.warn("User {} not found during comment operation", userId);
            throw new NotFoundException(userId, User.class);
        }
    }

    private void checkEvent(Long eventId) {
        if (!eventRepository.existsById(eventId)) {
            log.warn("Event {} not found during comment operation", eventId);
            throw new NotFoundException(eventId, Event.class);
        }
        if (!eventRepository.getReferenceById(eventId).getState().equals(PUBLISHED)) {
            throw new IncorrectEventStateException("Можно комментировать только опубликованные сообщения");
        }
    }

    private void checkComment(Long commentId) {
        if (!commentRepository.existsById(commentId)) {
            log.warn("Comment {} not found during comment operation", commentId);
            throw new NotFoundException(commentId, Comment.class);
        }
    }

}
