package ru.practicum.shareit.integrationTest;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserServiceIntegrationTest {
//    private final UserService userService;
//
//    @Test
//    void shouldNotAddUserIfEmailDuplicate() {
//        UserDto userRequest1 = UserDto.builder().name("test").email("uniqueemail@mail.ru").build();
//        UserDto userRequest2 = UserDto.builder().name("test2").email("uniqueemail@mail.ru").build();
//
//        UserDtoResponse user1 = userService.addUser(userRequest1);
//
//        assertThrows(DataIntegrityViolationException.class, () -> userService.addUser(userRequest2));
//
//        User foundUser1 = userService.findUserById(1L);
//        assertEquals("uniqueemail@mail.ru", user1.getEmail());
//        assertNotNull(foundUser1);
//        assertThrows(EntityNotFoundException.class, () -> userService.findUserById(9001L));
//    }
}
