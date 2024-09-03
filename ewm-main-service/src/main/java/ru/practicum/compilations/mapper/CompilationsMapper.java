package ru.practicum.compilations.mapper;

import org.mapstruct.Mapper;
import ru.practicum.compilations.dto.CompilationsDtoResponse;
import ru.practicum.compilations.model.CompilationsEntity;
import ru.practicum.event.mapper.EventMapper;

@Mapper(componentModel = "spring", uses = {EventMapper.class})
public interface CompilationsMapper {
    CompilationsDtoResponse fromCompilationsEntityToCompilationsDtoResponse(CompilationsEntity compilationsEntity);
}