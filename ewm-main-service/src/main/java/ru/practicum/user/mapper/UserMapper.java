package ru.practicum.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ru.practicum.user.dto.UserReqDto;
import ru.practicum.user.dto.UserRespDto;
import ru.practicum.user.dto.UserShortDto;
import ru.practicum.user.model.User;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    User toUserDto(UserReqDto userReqDto);

    UserRespDto toUserRespDto(User user);

    UserShortDto toUserShortDto(User user);
}
