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
import java.util.Collection;
import java.util.Optional;
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
    Optional<UserDto> addUser(@Valid @RequestBody final UserDto userDto) {
        log.info("Добавление пользователя");
        return service.addUser(UserMapper.mapToModel(userDto)).map(UserMapper::mapToDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/{id}")
    Optional<UserDto> getUser(@PathVariable final long id) {
        log.info("Вызов пользователя");
        return service.getUser(id).map(UserMapper::mapToDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    Collection<UserDto> getUsers() {
        log.info("Вызов всех пользователей");
        return service.getUsers().stream().map(UserMapper::mapToDto).collect(Collectors.toList());
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping(path = "/{id}")
    Optional<UserDto> updateUser(@RequestBody final UserDto userDto, @PathVariable final long id) {
        log.info("Обновление пользователя");
        return service.updateUser(UserMapper.mapToModel(userDto), id).map(UserMapper::mapToDto);
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    void deleteUser(@PathVariable final long id) {
        log.info("Удаление пользователя");
        service.deleteUser(id);
    }
}
