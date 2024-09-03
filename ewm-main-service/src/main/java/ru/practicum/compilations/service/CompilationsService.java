package ru.practicum.compilations.service;

import org.springframework.data.domain.Page;
import ru.practicum.compilations.dto.CompilationsDtoRequest;
import ru.practicum.compilations.dto.CompilationsDtoUpdate;
import ru.practicum.compilations.model.CompilationsEntity;

public interface CompilationsService {
    CompilationsEntity addCompilation(CompilationsDtoRequest compilationsDtoRequest);

    void deleteCompilation(Long compId);

    CompilationsEntity updateCompilation(Long compId, CompilationsDtoUpdate compilationsDtoUpdate);

    Page<CompilationsEntity> getAllCompilations(Boolean pinned, Integer from, Integer size);

    CompilationsEntity getCompilationById(Long compId);
}