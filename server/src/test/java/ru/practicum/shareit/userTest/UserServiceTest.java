package ru.practicum.shareit.userTest;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserServiceTest {
//
//    @Autowired
//    UserServiceImpl userService;
//
//    @MockBean
//    UserRepository userRepository;
//
//    User user1 = User.builder()
//            .id(1L)
//            .name("Варвара")
//            .email("varvara@gmail.com")
//            .build();
//
//    User user2 = User.builder()
//            .id(2L)
//            .name("Михаил")
//            .email("michael@gmail.com")
//            .build();
//
//
//    @Test
//    void shouldAddUser() {
//        UserDto request = UserDto.builder()
//                .name("Варвара")
//                .email("varvara@gmail.com")
//                .build();
//
//        when(userRepository.save(any(User.class))).thenReturn(user1);
//
//        userService.addUser(request);
//
//        verify(userRepository, times(1)).save(any(User.class));
//    }
//
//    @Test
//    void updateUserIfUserIdIsIncorrect() {
//        UserDto request = UserDto.builder()
//                .name("Варвара")
//                .email("varvara@gmail.com")
//                .build();
//
//        doThrow(EntityNotFoundException.class)
//                .when(userRepository).findById(anyLong());
//
//        assertThrows(EntityNotFoundException.class, () -> userService.updateUser(request, 1L));
//    }
//
//
//    @Test
//    void updateUserIfUserIdIsCorrect() {
//        UserDto request = UserDto.builder()
//                .name("Варвара новый")
//                .email("varvara@gmail.com")
//                .build();
//
//        User newUser = User.builder()
//                .id(1L)
//                .name("Варвара новый")
//                .email("varvara@gmail.com")
//                .build();
//
//        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user1));
//        when(userRepository.save(any(User.class))).thenReturn(newUser);
//
//        UserDtoResponse result = userService.updateUser(request, 1L);
//
//        assertThat(result.getName()).isEqualTo("Варвара новый");
//        assertThat(result.getEmail()).isEqualTo(request.getEmail());
//    }
//
//    @Test
//    void getAllUsersIfUsersExist() {
//        when(userRepository.findAll()).thenReturn(List.of(user1, user2));
//
//        List<UserDtoResponse> results = userService.getUsers();
//
//        verify(userRepository, times(1)).findAll();
//
//        assertFalse(results.isEmpty());
//    }
//
//    @Test
//    void getAllIfUsersNotExist() {
//        when(userRepository.findAll()).thenReturn(List.of());
//
//        List<UserDtoResponse> results = userService.getUsers();
//
//        verify(userRepository, times(1)).findAll();
//
//        assertTrue(results.isEmpty());
//    }
//
//    @Test
//    void getUserIfUserExist() {
//        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user1));
//
//        UserDtoResponse result = userService.getUser(1L);
//
//        verify(userRepository, times(1)).findById(anyLong());
//
//        assertNotNull(result);
//        assertThat(result.getName()).isEqualTo("Варвара");
//    }
//
//    @Test
//    void getUserIfUserIdIsIncorrect() {
//        doThrow(EntityNotFoundException.class)
//                .when(userRepository).findById(anyLong());
//
//        assertThrows(EntityNotFoundException.class, () -> userService.getUser(100L));
//    }
//
//    @Test
//    void findUserByIdIfUserIdIsIncorrect() {
//        doThrow(EntityNotFoundException.class)
//                .when(userRepository).findById(anyLong());
//
//        assertThrows(EntityNotFoundException.class, () -> userService.findUserById(100L));
//    }
//
//    @Test
//    void findUserByIdIfUserExist() {
//        when(userRepository.findById(anyLong()))
//                .thenReturn(Optional.ofNullable(user1));
//
//        User result = userService.findUserById(1L);
//
//        verify(userRepository, times(1)).findById(anyLong());
//        assertNotNull(result);
//        assertThat(result.getName()).isEqualTo("Варвара");
//    }
//
//    @Test
//    void deleteUserIfUserExist() {
//        when(userRepository.findById(anyLong()))
//                .thenReturn(Optional.ofNullable(user1));
//
//        userService.deleteUser(1L);
//
//        verify(userRepository, times(1)).deleteById(anyLong());
//    }
//
//    @Test
//    void deleteUserIfUserIdIsIncorrect() {
//        doThrow(EntityNotFoundException.class)
//                .when(userRepository).findById(anyLong());
//
//        assertThrows(EntityNotFoundException.class, () -> userService.deleteUser(100L));
//    }
//
//    @Test
//    void checkIfUserIdIsIncorrect() {
//        when(userRepository.findById(anyLong()))
//                .thenReturn(Optional.empty());
//
//        assertThrows(EntityNotFoundException.class, () -> userService.checkIfUserExists(100L));
//    }
//
//    @Test
//    void checkIfUserExist() {
//        when(userRepository.findById(anyLong()))
//                .thenReturn(Optional.ofNullable(user1));
//
//        userService.checkIfUserExists(1L);
//        verify(userRepository, times(1)).findById(anyLong());
//    }
}
