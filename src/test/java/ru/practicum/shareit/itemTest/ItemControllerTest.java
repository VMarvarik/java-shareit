package ru.practicum.shareit.itemTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.practicum.shareit.comment.dto.RequestCommentDto;
import ru.practicum.shareit.comment.dto.ResponseCommentDto;
import ru.practicum.shareit.exception.AccessDenied;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
class ItemControllerTest {
    @Autowired
    MockMvc mvc;

    @MockBean
    ItemService itemService;

    @Autowired
    ObjectMapper mapper;

    String USER_HEADER = "X-Sharer-User-Id";

    @Test
    void addItemShouldBeOk() throws Exception {
        ItemRequestDto itemRequest = ItemRequestDto.builder()
                .id(1L)
                .name("Лопата")
                .description("Лопата для сада")
                .available(true)
                .build();

        ItemResponseDto itemResponse = ItemResponseDto.builder()
                .id(1L)
                .name("Лопата")
                .description("Лопата для сада")
                .build();

        when(itemService.addItem(any(ItemRequestDto.class), anyLong()))
                .thenReturn(itemResponse);

        mvc.perform(post("/items")
                        .header(USER_HEADER, 1)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsString(itemRequest)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Лопата"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Лопата для сада"));
    }

    @Test
    void updateItemIfItemNotFound() throws Exception {
        ItemRequestDto itemRequest = ItemRequestDto.builder()
                .id(1L)
                .name("Лопата")
                .description("Лопата для сада")
                .build();


        doThrow(EntityNotFoundException.class)
                .when(itemService)
                .updateItem(any(ItemRequestDto.class), anyLong(), anyLong());

        mvc.perform(patch("/items/1")
                        .header(USER_HEADER, 1)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsString(itemRequest)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void updateItemIfUserIsNotOwnerItem() throws Exception {
        ItemRequestDto itemRequest = ItemRequestDto.builder()
                .id(1L)
                .name("Лопата")
                .description("Лопата для сада")
                .build();

        doThrow(AccessDenied.class)
                .when(itemService)
                .updateItem(any(ItemRequestDto.class), anyLong(), anyLong());

        mvc.perform(patch("/items/1")
                        .header(USER_HEADER, 1)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsString(itemRequest)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    void getAllItemsByUserIdIfItemsEmpty() throws Exception {
        when(itemService.getOwnerItems(anyLong()))
                .thenReturn(List.of());

        mvc.perform(MockMvcRequestBuilders.get("/items")
                        .header(USER_HEADER, 1))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").doesNotExist());
    }

    @Test
    void getAllItemsByUserIdIffUserDoesNotExist() throws Exception {
        doThrow(EntityNotFoundException.class)
                .when(itemService)
                .getOwnerItems(anyLong());

        mvc.perform(MockMvcRequestBuilders.get("/items")
                        .header(USER_HEADER, 1))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void getItemIfItemDoesNotExist() throws Exception {
        doThrow(EntityNotFoundException.class)
                .when(itemService)
                .getItem(anyLong(), anyLong());

        mvc.perform(MockMvcRequestBuilders.get("/items/1")
                        .header(USER_HEADER, 1))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void getItemIfItemExists() throws Exception {
        ItemResponseDto itemResponse = ItemResponseDto.builder()
                .id(1L)
                .name("Лопата")
                .description("Лопата для сада")
                .build();

        when(itemService.getItem(anyLong(), anyLong()))
                .thenReturn(itemResponse);

        mvc.perform(MockMvcRequestBuilders.get("/items/1")
                        .header(USER_HEADER, 1))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Лопата"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Лопата для сада"));
    }

    @Test
    void searchByQueryShouldBeOk() throws Exception {
        when(itemService.searchAvailableItems(anyString()))
                .thenReturn(List.of());

        mvc.perform(MockMvcRequestBuilders.get("/items/search")
                        .param("text", ""))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").doesNotExist());
    }

    @Test
    void searchByQueryIfTextIsEmpty() throws Exception {
        ItemResponseDto itemResponse = ItemResponseDto.builder()
                .id(1L)
                .name("Лопата")
                .description("Лопата для сада")
                .build();

        when(itemService.searchAvailableItems(anyString()))
                .thenReturn(List.of(itemResponse));

        mvc.perform(MockMvcRequestBuilders.get("/items/search")
                        .param("text", "book"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Лопата"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description").value("Лопата для сада"));
    }

    @Test
    void addCommentIfItemExists() throws Exception {
        RequestCommentDto commentRequest = RequestCommentDto.builder()
                .text("хорошо")
                .build();

        ResponseCommentDto commentResponse = ResponseCommentDto.builder()
                .id(1L)
                .authorName("User001")
                .text("хорошо")
                .created(LocalDateTime.of(2023, 5, 11, 17, 10, 36))
                .build();

        when(itemService.addComment(any(RequestCommentDto.class), anyLong(), anyLong()))
                .thenReturn(commentResponse);

        mvc.perform(post("/items/1/comment")
                        .header(USER_HEADER, 1)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsString(commentRequest)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.text").value("хорошо"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.created").value(commentResponse.getCreated().toString()));
    }

    @Test
    void addCommentItemDoesNotExist() throws Exception {
        RequestCommentDto commentRequest = RequestCommentDto.builder()
                .text("хорошо")
                .build();

        doThrow(AccessDenied.class)
                .when(itemService)
                .addComment(any(RequestCommentDto.class), anyLong(), anyLong());

        mvc.perform(post("/items/1/comment")
                        .header(USER_HEADER, 1)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsString(commentRequest)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").doesNotExist());
    }

    @Nested
    class ValidationItemTest {
        Validator validator;

        @BeforeEach
        void beforeEach() {
            try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
                validator = validatorFactory.getValidator();
            }
        }

        @Test
        void notValidIfItemIncorrect() {
            ItemRequestDto test = new ItemRequestDto();

            assertEquals(3, validator.validate(test).size());
        }

        @Test
        void notValidIfNameIsEmpty() {
            ItemRequestDto test = ItemRequestDto.builder().name("").description("description").available(true).build();

            List<ConstraintViolation<ItemRequestDto>> validationSet = new ArrayList<>(validator.validate(test));
            assertAll(
                    () -> assertEquals(1, validationSet.size()),
                    () -> assertEquals("Название вещи не может быть пустым", validationSet.get(0).getMessage())
            );
        }

        @Test
        void notValidNameIsNull() {
            ItemRequestDto test = ItemRequestDto.builder().name(null).description("description").available(true).build();

            List<ConstraintViolation<ItemRequestDto>> validationSet = new ArrayList<>(validator.validate(test));
            assertAll(
                    () -> assertEquals(1, validationSet.size()),
                    () -> assertEquals("Название вещи не может быть пустым", validationSet.get(0).getMessage())
            );
        }

        @Test
        void notValidIfDescriptionIsEmpty() {
            ItemRequestDto test = ItemRequestDto.builder().name("name").description("").available(true).build();

            List<ConstraintViolation<ItemRequestDto>> validationSet = new ArrayList<>(validator.validate(test));
            assertAll(
                    () -> assertEquals(1, validationSet.size()),
                    () -> assertEquals("Описание вещи не может быть пустым.", validationSet.get(0).getMessage())
            );
        }

        @Test
        void notValidIfDescriptionIsNull() {
            ItemRequestDto test = ItemRequestDto.builder().name("name").description(null).available(true).build();

            List<ConstraintViolation<ItemRequestDto>> validationSet = new ArrayList<>(validator.validate(test));
            assertAll(
                    () -> assertEquals(1, validationSet.size()),
                    () -> assertEquals("Описание вещи не может быть пустым.", validationSet.get(0).getMessage())
            );
        }

        @Test
        void notValidIfAvailableIsNull() {
            ItemRequestDto test = ItemRequestDto.builder().name("name").description("description").available(null).build();

            List<ConstraintViolation<ItemRequestDto>> validationSet = new ArrayList<>(validator.validate(test));
            assertAll(
                    () -> assertEquals(1, validationSet.size()),
                    () -> assertEquals("Статус вещи не может быть null.", validationSet.get(0).getMessage())
            );
        }

        @Test
        void notValidCommentIfTextNull() {
            RequestCommentDto test = RequestCommentDto.builder().text(null).build();

            List<ConstraintViolation<RequestCommentDto>> validationSet = new ArrayList<>(validator.validate(test));
            assertAll(
                    () -> assertEquals(1, validationSet.size()),
                    () -> assertEquals("must not be blank", validationSet.get(0).getMessage())
            );
        }
    }
}
