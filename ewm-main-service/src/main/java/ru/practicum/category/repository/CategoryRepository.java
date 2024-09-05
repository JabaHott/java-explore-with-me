package ru.practicum.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.category.model.Category;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByName(String name);

    @Query("SELECT COUNT(c) FROM Category c WHERE c.id IN (:ids)")
    Long countByIdIn(@Param("ids") List<Long> ids);
}
