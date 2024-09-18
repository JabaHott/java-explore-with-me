package ru.practicum.comment.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.comment.model.Comment;


import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findAllByEvent_Id(Long eventId, Pageable pageable);

    @Query("SELECT c FROM Comment c WHERE c.event.id IN :eventIds")
    List<Comment> getEventsComments(@Param("eventIds") List<Long> eventIds);

    @Query("select c from Comment as c where " +
            "(:user is null or c.author.id = :user) " +
            "and (:event is null or c.event.id in (:event)) " +
            "and (:text is null or lower(c.body) like %:text%)")
    Page<Comment> findAllCommentsWithFilterForAdmin(@Param("user") Long userId,
                                                    @Param("event") Long eventId,
                                                    @Param("text") String text,
                                                    Pageable pageable);
}

