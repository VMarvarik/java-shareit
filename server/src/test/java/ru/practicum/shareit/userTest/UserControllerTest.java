package ru.practicum.shareit.userTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.user.controller.UserController;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    UserService userService;

    @Autowired
    ObjectMapper mapper;

    @Test
    void shouldAddUser() throws Exception {
        UserDto userRequest = UserDto.builder()
                .name("Варвара")
                .email("varvara@gmail.com")
                .build();

        User user = User.builder()
                .id(1L)
                .name("Варвара")
                .email("varvara@gmail.com")
                .build();

        when(userService.addUser(any(UserDto.class)))
                .thenReturn(UserMapper.mapToDto(user));

        mvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsString(userRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Варвара"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").isString())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("varvara@gmail.com"));
    }

    @Test
    void updateUserIfUserNotFound() throws Exception {
        UserDto userRequest = UserDto.builder()
                .id(1L)
                .name("Варвара")
                .email("varvara@gmail.com")
                .build();


        doThrow(EntityNotFoundException.class)
                .when(userService)
                .updateUser(any(UserDto.class), anyLong());

        mvc.perform(patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsString(userRequest)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldUpdateUser() throws Exception {
        UserDto userRequest = UserDto.builder()
                .id(1L)
                .name("Варвара")
                .email("varvara@gmail.com")
                .build();

        User user = User.builder()
                .id(1L)
                .name("Варвара")
                .email("varvara@gmail.com")
                .build();

        when(userService.updateUser(any(UserDto.class), anyLong()))
                .thenReturn(UserMapper.mapToDto(user));

        mvc.perform(patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsString(userRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Варвара"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").isString())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("varvara@gmail.com"));
    }

    @Test
    void shouldDeleteUser() throws Exception {
        doNothing()
                .when(userService)
                .deleteUser(anyLong());

        mvc.perform(delete("/users/1"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void getAllUsersIfUsersDoNotExist() throws Exception {
        when(userService.getUsers())
                .thenReturn(List.of());

        mvc.perform(MockMvcRequestBuilders.get("/users"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").doesNotExist());
    }

    @Test
    void getAllUsersIfUsersExist() throws Exception {
        User user = User.builder()
                .id(1L)
                .name("Варвара")
                .email("varvara@gmail.com")
                .build();

        when(userService.getUsers())
                .thenReturn(UserMapper.mapToDto(List.of(user)));

        mvc.perform(MockMvcRequestBuilders.get("/users"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Варвара"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].email").isString())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].email").value("varvara@gmail.com"));
    }

    @Test
    void getUserIfUserDoesNotExist() throws Exception {
        doThrow(EntityNotFoundException.class)
                .when(userService)
                .getUser(anyLong());

        mvc.perform(MockMvcRequestBuilders.get("/users/1"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").doesNotExist());
    }

    @Test
    void getUserIfUserExists() throws Exception {
        User user = User.builder()
                .id(1L)
                .name("Варвара")
                .email("varvara@gmail.com")
                .build();

        when(userService.getUser(anyLong()))
                .thenReturn(UserMapper.mapToDto(user));

        mvc.perform(MockMvcRequestBuilders.get("/users/1"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Варвара"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").isString())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("varvara@gmail.com"));
    }
}