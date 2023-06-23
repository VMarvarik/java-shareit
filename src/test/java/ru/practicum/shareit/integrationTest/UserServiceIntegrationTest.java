package ru.practicum.shareit.integrationTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserDtoResponse;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserServiceIntegrationTest {
    private final UserService userService;

    @Test
    void shouldNotAddUserIfEmailDuplicate() {
        UserDto userRequest1 = UserDto.builder().name("test").email("uniqueemail@mail.ru").build();
        UserDto userRequest2 = UserDto.builder().name("test2").email("uniqueemail@mail.ru").build();

        UserDtoResponse user1 = userService.addUser(userRequest1);

        assertThrows(DataIntegrityViolationException.class, () -> userService.addUser(userRequest2));

        User foundUser1 = userService.findUserById(1L);
        assertEquals("uniqueemail@mail.ru", user1.getEmail());
        assertNotNull(foundUser1);
        //assertThrows(EntityNotFoundException.class, () -> userService.findUserById(2L));
    }
}
