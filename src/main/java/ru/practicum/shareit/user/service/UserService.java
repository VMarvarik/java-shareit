package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserDtoResponse;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
    UserDtoResponse addUser(UserDto userDto);

    UserDtoResponse getUser(Long id);

    List<UserDtoResponse> getUsers();

    UserDtoResponse updateUser(UserDto userDto, Long id);

    void deleteUser(Long id);

    User findUserById(Long userId);

    void checkIfUserExists(Long userId);
}
