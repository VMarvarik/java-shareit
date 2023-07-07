package ru.practicum.shareit.requestTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.InvalidRequestException;
import ru.practicum.shareit.item.dto.ItemDtoForRequest;
import ru.practicum.shareit.request.controller.RequestController;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.service.RequestService;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(RequestController.class)
class RequestControllerTest {
    @Autowired
    MockMvc mvc;

    @MockBean
    RequestService requestService;

    @Autowired
    ObjectMapper mapper;

    String userHeader = "X-Sharer-User-Id";

    @Test
    void addItemCorrectRequest() throws Exception {
        ItemDtoForRequest itemDto = ItemDtoForRequest.builder()
                .id(10L)
                .name("Лопата")
                .description("Лопата для сада")
                .available(true)
                .requestId(1L)
                .build();

        RequestDto request = RequestDto.builder()
                .description("Мне нужна лопата")
                .build();

        RequestDto requestDto = RequestDto.builder()
                .description("Мне нужна лопата")
                .requestorId(1L)
                .created(LocalDateTime.now().plusDays(10))
                .items(List.of(itemDto))
                .build();

        when(requestService.addRequest(any(RequestDto.class), anyLong()))
                .thenReturn(requestDto);

        mvc.perform(post("/requests")
                        .header(userHeader, 1)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Мне нужна лопата"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name").value("Лопата"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.requestorId").value(1L));
    }

    @Test
    void tryGetRequestShouldReturnOk() throws Exception {
        ItemDtoForRequest itemDto = ItemDtoForRequest.builder()
                .id(10L)
                .name("Лопата")
                .description("Лопата для сада")
                .available(true)
                .requestId(1L)
                .build();

        RequestDto requestDto = RequestDto.builder()
                .description("Мне нужна лопата")
                .requestorId(1L)
                .created(LocalDateTime.now().plusDays(10))
                .items(List.of(itemDto))
                .build();

        when(requestService.getAllRequestsByUserId(anyLong()))
                .thenReturn(List.of(requestDto));

        mvc.perform(get("/requests")
                        .header(userHeader, 1))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description").value("Мне нужна лопата"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].items[0].name").value("Лопата"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].requestorId").value(1L));
    }

    @Test
    void tryGetRequestByUserShouldReturnOk() throws Exception {
        ItemDtoForRequest itemDto = ItemDtoForRequest.builder()
                .id(10L)
                .name("Лопата")
                .description("Лопата для сада")
                .available(true)
                .requestId(1L)
                .build();

        RequestDto requestDto = RequestDto.builder()
                .description("Мне нужна лопата")
                .requestorId(1L)
                .created(LocalDateTime.now().plusDays(10))
                .items(List.of(itemDto))
                .build();

        when(requestService.getRequestByUserId(anyLong(), anyLong()))
                .thenReturn(requestDto);

        mvc.perform(get("/requests/1")
                        .header(userHeader, 1))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Мне нужна лопата"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name").value("Лопата"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.requestorId").value(1L));
    }

    @Test
    void tryGetRequestByOtherUserShouldBeOk() throws Exception {
        ItemDtoForRequest itemDto = ItemDtoForRequest.builder()
                .id(10L)
                .name("Лопата")
                .description("Лопата для сада")
                .available(true)
                .requestId(1L)
                .build();

        RequestDto requestDto = RequestDto.builder()
                .description("Мне нужна лопата")
                .requestorId(1L)
                .created(LocalDateTime.now().plusDays(10))
                .items(List.of(itemDto))
                .build();

        when(requestService.getRequestsByOtherUsers(anyLong(), anyInt(), anyInt()))
                .thenReturn(List.of(requestDto));

        mvc.perform(get("/requests/all")
                        .header(userHeader, 1)
                        .param("from", "1")
                        .param("size", "1"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description").value("Мне нужна лопата"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].items[0].name").value("Лопата"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].requestorId").value(1L));
    }

    @Test
    void tryGetOwnerRequestsShouldBeEmpty() throws Exception {
        when(requestService.getAllRequestsByUserId(anyLong()))
                .thenReturn(List.of());

        mvc.perform(get("/requests")
                        .header(userHeader, 1))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isEmpty());

    }

    @Test
    void tryGetRequestUserDoesNotExist() throws Exception {
        doThrow(EntityNotFoundException.class)
                .when(requestService)
                .getRequestById(anyLong());

        mvc.perform(get("/requests")
                        .header(userHeader, 1))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void tryGetRequestByUserNotFound() throws Exception {
        doThrow(EntityNotFoundException.class)
                .when(requestService)
                .getRequestByUserId(anyLong(), anyLong());

        mvc.perform(get("/requests/1")
                        .header(userHeader, 1))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void tryGetRequestIfIncorrectPageable() throws Exception {
        doThrow(InvalidRequestException.class)
                .when(requestService)
                .getRequestsByOtherUsers(anyLong(), anyInt(), anyInt());

        mvc.perform(get("/requests/all")
                        .header(userHeader, 1)
                        .param("from", "-1")
                        .param("size", "-1"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void tryGetRequestByOtherUserNotFoundReturnsStatusOk() throws Exception {
        when(requestService.getRequestsByOtherUsers(anyLong(), anyInt(), anyInt()))
                .thenReturn(List.of());

        mvc.perform(get("/requests/all")
                        .header(userHeader, 1)
                        .param("from", "1")
                        .param("size", "1"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

//    @Nested
//    class ValidationUserTest {
//
//        Validator validator;
//
//        @BeforeEach
//        void beforeEach() {
//            try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
//                validator = validatorFactory.getValidator();
//            }
//        }
//
//        @Test
//        void addIncorrectRequest() {
//            RequestDto test = new RequestDto();
//
//            assertEquals(1, validator.validate(test).size());
//        }
//
//        @Test
//        void addRequestIncorrectDescription() {
//            RequestDto test = RequestDto.builder().description("").build();
//
//            List<ConstraintViolation<RequestDto>> validationSet = new ArrayList<>(validator.validate(test));
//            assertAll(
//                    () -> assertEquals(1, validationSet.size()),
//                    () -> assertEquals("must not be blank", validationSet.get(0).getMessage())
//            );
//        }
//    }
}
