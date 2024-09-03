package ru.practicum.user.controller;


import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.user.dto.UserReqDto;
import ru.practicum.user.dto.UserRespDto;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.model.User;
import ru.practicum.user.service.UserService;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/admin/users")
@Validated
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper mapper;

    @GetMapping
    public List<UserRespDto> findAllUsers(
            @PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer from,
            @PositiveOrZero @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(name = "ids", required = false) List<Long> usersIds
    ) {
        log.info("Получен GET запрос /users с params {}, {}, {}", from, size, usersIds);
        Page<User> response = userService.findAllUsers(from, size, usersIds != null ? usersIds : Collections.emptyList());

        List<UserRespDto> userRespDtos = response.stream().map(mapper::toUserRespDto).collect(Collectors.toList());

        return ok(userRespDtos).getBody();
    }

    @PostMapping
    public ResponseEntity<UserRespDto> addUser(@Valid @RequestBody UserReqDto userReqDto) {
        log.info("Получен POST запрос /users с телом {}", userReqDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toUserRespDto(userService.addUser(
                mapper.toUserDto(userReqDto)
        )));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PositiveOrZero @PathVariable Long userId) {
        log.info("Получен DELETE запрос /users с телом {}", userId);
        userService.deleteUser(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
