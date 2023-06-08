package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/users")
@Slf4j
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService service;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    UserDto addUser(@Valid @RequestBody UserDto userDto) {
        log.info("Добавление пользователя");
        return UserMapper.mapToDto(service.addUser(UserMapper.mapToModel(userDto)));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/{id}")
    UserDto getUser(@PathVariable long id) {
        log.info("Вызов пользователя");
        return UserMapper.mapToDto(service.getUser(id));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    List<UserDto> getUsers() {
        log.info("Вызов всех пользователей");
        return service.getUsers().stream().map(UserMapper::mapToDto).collect(Collectors.toList());
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping(path = "/{id}")
    UserDto updateUser(@RequestBody UserDto userDto, @PathVariable long id) {
        log.info("Обновление пользователя");
        return UserMapper.mapToDto(service.updateUser(UserMapper.mapToModel(userDto), id));
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    void deleteUser(@PathVariable long id) {
        log.info("Удаление пользователя");
        service.deleteUser(id);
    }
}
