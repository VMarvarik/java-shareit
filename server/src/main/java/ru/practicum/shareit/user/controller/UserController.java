package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @ResponseStatus(code = HttpStatus.CREATED)
    @PostMapping
    public UserDto addUser(@RequestBody UserDto userDto) {
        log.info("Добавление пользователя");
        return userService.addUser(userDto);
    }

    @ResponseStatus(code = HttpStatus.OK)
    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable Long id) {
        log.info("Вызов пользователя");
        return userService.getUser(id);
    }

    @ResponseStatus(code = HttpStatus.OK)
    @GetMapping
    public List<UserDto> getUsers() {
        log.info("Вызов всех пользователей");
        return userService.getUsers();
    }

    @ResponseStatus(code = HttpStatus.OK)
    @PatchMapping("/{id}")
    public UserDto updateUser(@RequestBody UserDto userDto, @PathVariable Long id) {
        log.info("Обновление пользователя");
        return userService.updateUser(userDto, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public void deleteUser(@PathVariable Long id) {
        log.info("Удаление пользователя");
        userService.deleteUser(id);
    }
}
