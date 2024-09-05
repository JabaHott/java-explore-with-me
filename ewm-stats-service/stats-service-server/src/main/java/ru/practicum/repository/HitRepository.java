package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.model.Hit;
import ru.practicum.model.StatsModel;

import java.time.LocalDateTime;
import java.util.List;

public interface HitRepository extends JpaRepository<Hit, Long> {

    @Query("SELECT new ru.practicum.model.StatsModel(h.app, h.uri, count(h.ip)) " +
            "FROM Hit as h " +
            "WHERE h.timestamp BETWEEN :start AND :end " +
            "AND ((:uris) IS NULL OR h.uri IN (:uris)) " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY count(h.ip) DESC")
    List<StatsModel> getStats(@Param("start") LocalDateTime start,
                              @Param("end") LocalDateTime end,
                              @Param("uris") List<String> uris);

    @Query("SELECT new ru.practicum.model.StatsModel(h.app, h.uri, count(DISTINCT h.ip)) " +
            "FROM Hit as h " +
            "WHERE h.timestamp BETWEEN :start AND :end " +
            "AND ((:uris) IS NULL OR h.uri IN (:uris)) " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY count(DISTINCT h.ip) DESC")
    List<StatsModel> getUniqueStats(@Param("start") LocalDateTime start,
                                    @Param("end") LocalDateTime end,
                                    @Param("uris") List<String> uris);

    @Query("SELECT count(DISTINCT h.ip) " +
            "FROM Hit AS h " +
            "WHERE h.uri = :uri " +
            "GROUP BY h.uri " +
            "ORDER BY count(DISTINCT h.ip) DESC")
    Long getViews(@Param("uri") String uri);

    @Query("SELECT h.uri AS uri, COUNT(DISTINCT h.ip) AS count " +
            "FROM Hit h " +
            "WHERE h.uri IN (:eventsId) " +
            "GROUP BY h.uri")
    List<Object[]> countDistinctIpsByEventsId(@Param("eventsId") List<String> eventsId);

}