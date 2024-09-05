package ru.practicum.comment.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.practicum.comment.dto.CommentsReqDto;
import ru.practicum.comment.dto.CommentsRespDto;
import ru.practicum.comment.model.Comment;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentMapper {
    Comment toComment(CommentsReqDto commentsReqDto);

    @Mapping(source = "published", target = "published", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(source = "author.id", target = "userId")
    @Mapping(source = "event.id", target = "eventId")
    CommentsRespDto toCommentRespDto(Comment comment);
 }
