package ru.practicum.shareit.bookingTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.booking.dto.RequestBookingDto;
import ru.practicum.shareit.booking.dto.ResponseBookingDto;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.InvalidRequestException;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(BookingController.class)
class BookingControllerTest {
    @Autowired
    MockMvc mvc;

    @MockBean
    BookingService bookingService;

    @Autowired
    ObjectMapper mapper;

    String userHeader = "X-Sharer-User-Id";

    @Test
    void getAllBookingsByBookerCorrectPageableParameters() throws Exception {
        ResponseBookingDto bookingResponse = ResponseBookingDto.builder()
                .id(10L)
                .start(LocalDateTime.now().plusDays(10))
                .end(LocalDateTime.now().plusDays(30))
                .status(BookingStatus.APPROVED)
                .booker(new ResponseBookingDto.Booker(1L, "Варвара"))
                .item(new ResponseBookingDto.Item(1L, "Лопата"))
                .build();

        when(bookingService.getAllByBooker(anyLong(), anyString(), anyInt(), anyInt()))
                .thenReturn(List.of(bookingResponse));

        mvc.perform(MockMvcRequestBuilders.get("/bookings")
                        .header(userHeader, 1)
                        .param("from", "1")
                        .param("size", "1")
                        .param("state", "ALL"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].status").value(BookingStatus.APPROVED.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].booker.name").value("Варвара"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].item.name").value("Лопата"));
    }

    @Test
    void getAllBookingsByBookerIncorrectPageableParameters() throws Exception {
        doThrow(InvalidRequestException.class)
                .when(bookingService)
                .getAllByBooker(anyLong(), anyString(), anyInt(), anyInt());

        mvc.perform(MockMvcRequestBuilders.get("/bookings")
                        .header(userHeader, 1)
                        .param("from", "-1")
                        .param("size", "-1")
                        .param("state", "ALL"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0]").doesNotExist());
    }

    @Test
    void getAllBookingsByOwnerCorrectPageableParameters() throws Exception {
        ResponseBookingDto bookingResponse = ResponseBookingDto.builder()
                .id(10L)
                .start(LocalDateTime.now().plusDays(10))
                .end(LocalDateTime.now().plusDays(30))
                .status(BookingStatus.APPROVED)
                .booker(new ResponseBookingDto.Booker(1L, "Варвара"))
                .item(new ResponseBookingDto.Item(1L, "Лопата"))
                .build();

        when(bookingService.getAllByOwner(anyLong(), anyString(), anyInt(), anyInt()))
                .thenReturn(List.of(bookingResponse));

        mvc.perform(MockMvcRequestBuilders.get("/bookings/owner")
                        .header(userHeader, 1)
                        .param("from", "1")
                        .param("size", "1")
                        .param("state", "ALL"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].status").value(BookingStatus.APPROVED.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].booker.name").value("Варвара"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].item.name").value("Лопата"));
    }

    @Test
    void getAllBookingsByOwnerIncorrectPageableParameters() throws Exception {
        doThrow(InvalidRequestException.class)
                .when(bookingService)
                .getAllByOwner(anyLong(), anyString(), anyInt(), anyInt());

        mvc.perform(MockMvcRequestBuilders.get("/bookings/owner")
                        .header(userHeader, 1)
                        .param("from", "-1")
                        .param("size", "-1")
                        .param("state", "ALL"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0]").doesNotExist());
    }

    @Test
    void addBookingStatusNotFoundIfItemDoesNotExist() throws Exception {
        RequestBookingDto requestBookingDto = RequestBookingDto.builder()
                .itemId(1L)
                .start(LocalDateTime.now().plusDays(10))
                .end(LocalDateTime.now().plusDays(30))
                .build();

        doThrow(EntityNotFoundException.class)
                .when(bookingService).addBooking(anyLong(), any(RequestBookingDto.class));

        mvc.perform(post("/bookings")
                        .header(userHeader, 1)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsString(requestBookingDto)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").doesNotExist());
    }

    @Test
    void addBookingShouldReturnBadRequestIfStatusIsNotAvailable() throws Exception {
        RequestBookingDto bookingRequest = RequestBookingDto.builder()
                .itemId(1L)
                .start(LocalDateTime.now().plusDays(10))
                .end(LocalDateTime.now().plusDays(30))
                .build();

        doThrow(InvalidRequestException.class)
                .when(bookingService).addBooking(anyLong(), any(RequestBookingDto.class));

        mvc.perform(post("/bookings")
                        .header(userHeader, 1)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsString(bookingRequest)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").doesNotExist());
    }

    @Test
    void addBookingShouldReturnBadRequestIfTimeIsIncorrect() throws Exception {
        RequestBookingDto bookingRequest = RequestBookingDto.builder()
                .itemId(1L)
                .start(LocalDateTime.now().plusDays(10))
                .end(LocalDateTime.now().minusDays(5))
                .build();

        doThrow(InvalidRequestException.class)
                .when(bookingService).addBooking(anyLong(), any(RequestBookingDto.class));

        mvc.perform(post("/bookings")
                        .header(userHeader, 1)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsString(bookingRequest)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").doesNotExist());
    }

    @Test
    void addBookingShouldReturnCreated() throws Exception {
        RequestBookingDto bookingRequest = RequestBookingDto.builder()
                .itemId(1L)
                .start(LocalDateTime.now().plusDays(10))
                .end(LocalDateTime.now().plusDays(30))
                .build();

        ResponseBookingDto bookingResponse = ResponseBookingDto.builder()
                .id(10L)
                .start(LocalDateTime.now().plusDays(10))
                .end(LocalDateTime.now().plusDays(30))
                .booker(new ResponseBookingDto.Booker(1L, "Варвара"))
                .item(new ResponseBookingDto.Item(1L, "Лопата"))
                .build();

        when(bookingService.addBooking(anyLong(), any(RequestBookingDto.class)))
                .thenReturn(bookingResponse);

        mvc.perform(post("/bookings")
                        .header(userHeader, 1)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsString(bookingRequest)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.booker.name").value("Варвара"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.item.name").value("Лопата"));
    }

    @Test
    void tryChangeBookingStatusStatusNotFound() throws Exception {

        doThrow(EntityNotFoundException.class)
                .when(bookingService).updateStatus(anyLong(), anyBoolean(), anyLong());

        mvc.perform(patch("/bookings/1")
                        .header(userHeader, 1)
                        .param("approved", "true"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").doesNotExist());
    }

    @Test
    void tryChangeBookingStatusStatusBadRequest() throws Exception {

        doThrow(InvalidRequestException.class)
                .when(bookingService).updateStatus(anyLong(), anyBoolean(), anyLong());

        mvc.perform(patch("/bookings/1")
                        .header(userHeader, 1)
                        .param("approved", "true"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").doesNotExist());
    }

    @Test
    void tryChangeBookingStatusStatusOk() throws Exception {
        ResponseBookingDto bookingResponse = ResponseBookingDto.builder()
                .id(10L)
                .start(LocalDateTime.now().plusDays(10))
                .end(LocalDateTime.now().plusDays(30))
                .status(BookingStatus.APPROVED)
                .booker(new ResponseBookingDto.Booker(1L, "Варвара"))
                .item(new ResponseBookingDto.Item(1L, "Лопата"))
                .build();

        when(bookingService.updateStatus(anyLong(), anyBoolean(), anyLong()))
                .thenReturn(bookingResponse);

        mvc.perform(patch("/bookings/1")
                        .header(userHeader, 1)
                        .param("approved", "true"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(BookingStatus.APPROVED.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.booker.name").value("Варвара"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.item.name").value("Лопата"));
    }

    @Test
    void tryGetBookingIfBookingExists() throws Exception {
        ResponseBookingDto bookingResponse = ResponseBookingDto.builder()
                .id(10L)
                .start(LocalDateTime.now().plusDays(10))
                .end(LocalDateTime.now().plusDays(30))
                .status(BookingStatus.APPROVED)
                .booker(new ResponseBookingDto.Booker(1L, "Варвара"))
                .item(new ResponseBookingDto.Item(1L, "Лопата"))
                .build();

        when(bookingService.findById(anyLong(), anyLong()))
                .thenReturn(bookingResponse);

        mvc.perform(MockMvcRequestBuilders.get("/bookings/1")
                        .header(userHeader, 1))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(BookingStatus.APPROVED.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.booker.name").value("Варвара"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.item.name").value("Лопата"));
    }

    @Test
    void tryGetBookingIfBookingDoesNotExist() throws Exception {
        doThrow(EntityNotFoundException.class)
                .when(bookingService).findById(anyLong(), anyLong());

        mvc.perform(MockMvcRequestBuilders.get("/bookings/1")
                        .header(userHeader, 1))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").doesNotExist());
    }
}
