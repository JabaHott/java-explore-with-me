package ru.practicum.request.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.practicum.request.dto.RequestsDtoResponse;
import ru.practicum.request.model.RequestEntity;

import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RequestMapper {
    final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Mapping(source = "event.id", target = "event")
    @Mapping(source = "requester.id", target = "requester")
    RequestsDtoResponse fromRequestViewToRequestDtoResponse(RequestEntity requestEntity);

    @Mapping(source = "event.id", target = "event")
    @Mapping(source = "requester.id", target = "requester")
    RequestsDtoResponse fromRequestEntityToRequestDtoResponse(RequestEntity requestEntity);
}
