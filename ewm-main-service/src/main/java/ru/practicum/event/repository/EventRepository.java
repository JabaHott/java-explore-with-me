package ru.practicum.event.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.State;

import java.time.LocalDateTime;
import java.util.List;


public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("SELECT e FROM Event e WHERE e.initiator.id = :userId")
    Page<Event> findEventsByInitiatorId(@Param("userId") Long userId, Pageable pageable);

    @Query("select ev from Event as ev where " +
            "((:users) is null or ev.initiator.id in (:users)) " +
            "and ((:state) is null or ev.state in (:state)) " +
            "and ((:category) is null or ev.category.id in (:category))")
    Page<Event> getAllEventsWithoutDate(@Param("users") List<Long> users,
                                               @Param("state") List<State> state,
                                               @Param("category") List<Long> category,
                                               Pageable pageable);

    @Query("select ev from Event as ev where " +
            "((:users) is null or ev.initiator.id in (:users)) " +
            "and ((:state) is null or ev.state in (:state)) " +
            "and ((:category) is null or ev.category.id in (:category)) " +
            "and (ev.createdOn between :rangeStart and :rangeEnd)")
    Page<Event> getAllEvents(@Param("users") List<Long> users,
                                    @Param("state") List<State> state,
                                    @Param("category") List<Long> category,
                                    @Param("rangeStart") LocalDateTime rangeStart,
                                    @Param("rangeEnd") LocalDateTime rangeEnd,
                                    Pageable pageable);

    @Query("select ev from Event as ev where " +
            "(lower(ev.annotation) like :text or lower(ev.description) like :text) " +
            "and ((:category) is null or ev.category.id in (:category)) " +
            "and (:paid is null or ev.paid=:paid) " +
            "and (ev.eventDate > current_timestamp) " +
            "and ((:onlyAvailable = false) or (ev.participantLimit > ev.confirmedRequests)) " +
            "and (ev.state='PUBLISHED') " +
            "order by case when :sort='EVENT_DATE' then ev.eventDate end, " +
            "case when :sort='VIEWS' then ev.views end")
    Page<Event> getAllEventsWithSortWithoutDate(@Param("text") String text,
                                                       @Param("category") List<Long> category,
                                                       @Param("paid") Boolean paid,
                                                       @Param("onlyAvailable") Boolean onlyAvailable,
                                                       @Param("sort") String sort,
                                                       Pageable pageable);

    @Query("select ev from Event as ev where " +
            "(lower(ev.annotation) like :text) " +
            "and ((:category) is null or ev.category.id in (:category)) " +
            "and (:paid is null or ev.paid=:paid) " +
            "and (ev.eventDate between :start and :end) " +
            "and ((:onlyAvailable = false) or (ev.participantLimit > ev.confirmedRequests)) " +
            "and (ev.state='PUBLISHED') " +
            "order by case when :sort='EVENT_DATE' then ev.eventDate end, " +
            "case when :sort='VIEWS' then ev.views end")
    Page<Event> getAllEventsWithSortWithDate(@Param("text") String text,
                                                    @Param("category") List<Long> category,
                                                    @Param("paid") Boolean paid,
                                                    @Param("onlyAvailable") Boolean onlyAvailable,
                                                    @Param("start") LocalDateTime start,
                                                    @Param("end") LocalDateTime end,
                                                    @Param("sort") String sort,
                                                    Pageable pageable);


    @Query("select ev from Event as ev where " +
            "ev.id = :id and ev.state = :state")
    Event findByIdAndStates(@Param("id") Long id, @Param("state") State state);

    Event findByIdAndInitiator_Id(Long eventId, Long userId);



}
