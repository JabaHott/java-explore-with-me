package ru.practicum.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exception.NotFoundException;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
//    private final UserMapper mapper;

    @Override
    @Transactional
    public Page<User> findAllUsers(Integer from, Integer size, List<Long> usersIds) {
        if (usersIds == null) {
            log.info("Запрошен список всех пользователей");
            return userRepository.findAll(PageRequest.of(from, size));
        } else {
            log.info("Запрошен список пользователей {}", usersIds);
            return userRepository.findAllByIdIn(usersIds, PageRequest.of(from, size));
        }
    }

    @Override
    @Transactional
    public User addUser(User user) {
        checkEmail(user.getEmail());
        try {
            return userRepository.save(user);
        } catch (DataAccessException e) {
            log.warn("Error creating user");
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
        } else {
            log.info("Could not delete not existing user");
            throw new NotFoundException(userId, new User());
        }
    }

    @Override
    @Transactional
    public User findUserById(Long userId) {
        log.info("Запрошен пользователь {}", userId);
        return userRepository.findById(userId).orElseThrow(() -> new NotFoundException(userId, new User()));
    }

    private void checkEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new DataIntegrityViolationException("");
        }
    }
}