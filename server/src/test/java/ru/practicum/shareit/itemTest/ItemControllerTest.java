package ru.practicum.shareit.itemTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.practicum.shareit.comment.dto.RequestCommentDto;
import ru.practicum.shareit.exception.AccessDenied;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

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

    String userHeader = "X-Sharer-User-Id";

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
                        .header(userHeader, 1)
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
                        .header(userHeader, 1)
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
                        .header(userHeader, 1)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsString(itemRequest)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    void getItemIfItemDoesNotExist() throws Exception {
        doThrow(EntityNotFoundException.class)
                .when(itemService)
                .getItem(anyLong(), anyLong());

        mvc.perform(MockMvcRequestBuilders.get("/items/1")
                        .header(userHeader, 1))
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
                        .header(userHeader, 1))
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
    void addCommentItemDoesNotExist() throws Exception {
        RequestCommentDto commentRequest = RequestCommentDto.builder()
                .text("хорошо")
                .build();

        doThrow(AccessDenied.class)
                .when(itemService)
                .addComment(any(RequestCommentDto.class), anyLong(), anyLong());

        mvc.perform(post("/items/1/comment")
                        .header(userHeader, 1)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsString(commentRequest)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").doesNotExist());
    }
}