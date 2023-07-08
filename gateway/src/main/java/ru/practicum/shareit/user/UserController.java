package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/users")
@Slf4j
@RequiredArgsConstructor
@Validated
public class UserController {
    private final UserClient userClient;

    @GetMapping("/{userId}")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<Object> getUser(@PathVariable Long userId) {
        log.info("Вызов пользователя");
        return userClient.getUser(userId);
    }

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<Object> getUsers() {
        log.info("Вызов всех пользователей");
        return userClient.getUsers();
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity<Object> addUser(@RequestBody @Valid UserDto request) {
        log.info("Добавление пользователя");
        return userClient.addUser(request);
    }

    @PatchMapping("/{userId}")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<Object> updateUser(@RequestBody UserDto request,
                                             @PathVariable(name = "userId") Long userId) {
        log.info("Обновление пользователя");
        return userClient.updateUser(userId, request);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<Object> deleteUser(@PathVariable(name = "userId") Long userId) {
        log.info("Удаление пользователя");
        return userClient.deleteUser(userId);
    }
}