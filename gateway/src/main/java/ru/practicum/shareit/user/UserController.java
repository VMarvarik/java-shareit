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

    @ResponseStatus(code = HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<Object> addUser(@Valid @RequestBody UserDto userDto) {
        log.info("Добавление пользователя");
        return userClient.addUser(userDto);
    }

    @ResponseStatus(code = HttpStatus.OK)
    @GetMapping("/{id}")
    public ResponseEntity<Object> getUser(@PathVariable Long userId) {
        log.info("Вызов пользователя");
        return userClient.getUser(userId);
    }

    @ResponseStatus(code = HttpStatus.OK)
    @GetMapping
    public ResponseEntity<Object> getUsers() {
        log.info("Вызов всех пользователей");
        return userClient.getUsers();
    }

    @ResponseStatus(code = HttpStatus.OK)
    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateUser(@RequestBody UserDto userDto, @PathVariable Long userId) {
        log.info("Обновление пользователя");
        return userClient.updateUser(userId, userDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public void deleteUser(@PathVariable Long userId) {
        log.info("Удаление пользователя");
        userClient.deleteUser(userId);
    }
}
