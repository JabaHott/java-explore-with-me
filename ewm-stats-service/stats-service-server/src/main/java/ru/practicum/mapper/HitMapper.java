package ru.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;
import ru.practicum.HitRequestDto;
import ru.practicum.HitResponseDto;
import ru.practicum.model.Hit;

@Component
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface HitMapper {

    HitRequestDto toHitRequestDto(Hit hit);

    Hit toHit(HitRequestDto hitDto);

    HitResponseDto toHitResponseDto(Hit hit);

}
