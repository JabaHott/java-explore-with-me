package ru.practicum.event.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.category.model.Category;
import ru.practicum.event.dto.EventGetDto;
import ru.practicum.event.dto.EventRespDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.PatchAdminRespDto;
import ru.practicum.event.model.Event;
import ru.practicum.user.mapper.UserMapper;

import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring", uses = {CategoryMapper.class, UserMapper.class})
public interface EventMapper {
    DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Mapping(target = "eventDate", source = "eventDate", dateFormat = "yyyy-MM-dd HH:mm:ss")
    Event toEvent(NewEventDto newEventDto);

    @Mapping(source = "category", target = "category")
    @Mapping(target = "eventDate", source = "eventDate", dateFormat = "yyyy-MM-dd HH:mm:ss")
    EventRespDto toEventRespDto(Event eventsEntity);

    PatchAdminRespDto toPatchAdminRespDto(Event event);

    EventGetDto toEventGetDto(Event event);

    Category map(Long categoryId);

}