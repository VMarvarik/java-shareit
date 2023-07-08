package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
    UserDto addUser(UserDto userDto);

    UserDto getUser(Long id);

    List<UserDto> getUsers();

    UserDto updateUser(UserDto userDto, Long id);

    void deleteUser(Long id);

    User findUserById(Long userId);

    void checkIfUserExists(Long userId);
}
