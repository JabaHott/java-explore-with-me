package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.model.Hit;
import ru.practicum.model.StatsModel;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HitRepository extends JpaRepository<Hit, Long> {

    @Transactional
    @Query("SELECT new ru.practicum.model.StatsModel(h.app, h.uri, count(h.ip)) " +
            "FROM Hit as h " +
            "WHERE h.timestamp BETWEEN :start AND :end " +
            "AND ((:uris) IS NULL OR h.uri IN (:uris)) " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY count(h.ip) DESC")
    List<StatsModel> getStats(@Param("start") LocalDateTime start,
                              @Param("end") LocalDateTime end,
                              @Param("uris") List<String> uri);

    @Transactional
    @Query("SELECT new ru.practicum.model.StatsModel(h.app, h.uri, count(DISTINCT h.ip)) " +
            "FROM Hit as h " +
            "WHERE h.timestamp BETWEEN :start AND :end " +
            "AND ((:uris) IS NULL OR h.uri IN (:uris)) " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY count(DISTINCT h.ip) DESC")
    List<StatsModel> getUniqueStats(@Param("start") LocalDateTime start,
                                    @Param("end") LocalDateTime end,
                                    @Param("uris") List<String> uri);
}
