package ru.practicum.user.service;

import org.springframework.data.domain.Page;
import ru.practicum.user.model.User;

import java.util.List;

public interface UserService {
    Page<User> findAllUsers(Integer from, Integer size, List<Long> usersIds);

    User addUser(User user);

    void deleteUser(Long userId);

    User findUserById(Long userId);
}
