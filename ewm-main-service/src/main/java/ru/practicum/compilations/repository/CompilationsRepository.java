package ru.practicum.compilations.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.compilations.model.CompilationsEntity;


public interface CompilationsRepository extends JpaRepository<CompilationsEntity, Long> {
    @Query("select c from CompilationsEntity as c where " +
            "(:pinned is null or c.pinned = :pinned)")
    Page<CompilationsEntity> getAllCompilations(@Param("pinned") Boolean pinned, Pageable pageable);
}