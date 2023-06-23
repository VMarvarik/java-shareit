package ru.practicum.shareit.bookingTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.dto.RequestBookingDto;
import ru.practicum.shareit.booking.dto.ResponseBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.InvalidRequestException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class BookingServiceTest {

    @Autowired
    BookingServiceImpl bookingService;

    @MockBean
    BookingRepository bookingRepository;

    @MockBean
    UserService userService;

    @MockBean
    ItemRepository itemRepository;
    User user;
    User user2;
    Item item;
    Booking booking;

    @BeforeEach
    void beforeEach() {
        user = User.builder().id(1L).name("Варвара").email("varvara@gmail.com").build();
        user2 = User.builder().id(2L).name("Михаил").email("michael@gmail.com").build();

        item = Item.builder().id(1L)
                .name("Лопата")
                .description("Лопата для сада")
                .owner(user)
                .available(true)
                .build();

        booking = Booking.builder()
                .id(1L)
                .item(item)
                .start(LocalDateTime.of(2023, 2, 10, 17, 10, 5))
                .end(LocalDateTime.of(2023, 2, 10, 17, 10, 5).plusDays(15))
                .booker(user2)
                .build();
    }

    @Test
    void addBookingIfUserIdDoNotExist() {
        RequestBookingDto request = RequestBookingDto.builder()
                .itemId(1L)
                .start(LocalDateTime.of(2023, 2, 10, 17, 10, 5))
                .end(LocalDateTime.of(2023, 2, 10, 17, 10, 5).plusDays(15))
                .build();

        doThrow(EntityNotFoundException.class)
                .when(userService).findUserById(anyLong());

        assertThrows(EntityNotFoundException.class, () -> bookingService.addBooking(1L, request));
    }

    @Test
    void addBookingIfAvailableIsFalse() {
        item.setAvailable(false);

        RequestBookingDto request = RequestBookingDto.builder()
                .itemId(1L)
                .start(LocalDateTime.of(2023, 2, 10, 17, 10, 5))
                .end(LocalDateTime.of(2023, 2, 10, 17, 10, 5).plusDays(15))
                .build();

        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        assertThrows(InvalidRequestException.class, () -> bookingService.addBooking(1L, request));
    }

    @Test
    void addBookingIfEndIsBeforeStart() {
        RequestBookingDto request = RequestBookingDto.builder()
                .itemId(1L)
                .start(LocalDateTime.of(2023, 2, 10, 17, 10, 5))
                .end(LocalDateTime.of(2023, 2, 10, 17, 10, 5).minusDays(15))
                .build();

        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        assertThrows(InvalidRequestException.class, () -> bookingService.addBooking(1L, request));
    }

    @Test
    void addBookingIfOwnerAndRequestorIsTheSame() {
        RequestBookingDto request = RequestBookingDto.builder()
                .itemId(1L)
                .start(LocalDateTime.of(2023, 2, 10, 17, 10, 5))
                .end(LocalDateTime.of(2023, 2, 10, 17, 10, 5).plusDays(15))
                .build();

        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        assertThrows(EntityNotFoundException.class, () -> bookingService.addBooking(1L, request));
    }

    @Test
    void addBookingShouldBeOk() {
        RequestBookingDto request = RequestBookingDto.builder()
                .itemId(1L)
                .start(LocalDateTime.of(2023, 2, 10, 17, 10, 5))
                .end(LocalDateTime.of(2023, 2, 10, 17, 10, 5).plusDays(15))
                .build();

        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(item));

        when(bookingRepository.save(any(Booking.class)))
                .thenReturn(booking);

        bookingService.addBooking(2L, request);

        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    void getAllByItemIfBookingsExist() {
        when(bookingRepository.findAllByItemId(anyLong())).thenReturn(List.of(booking));
        List<Booking> result = bookingService.getAllByItemId(1L);
        verify(bookingRepository, times(1)).findAllByItemId(anyLong());
        assertFalse(result.isEmpty());
    }

    @Test
    void getAllByItemIfBookingsDoNotExist() {
        when(bookingRepository.findAllByItemId(anyLong())).thenReturn(List.of());
        List<Booking> result = bookingService.getAllByItemId(1L);
        verify(bookingRepository, times(1)).findAllByItemId(anyLong());
        assertTrue(result.isEmpty());
    }

    @Test
    void getAllByItemIdInIfBookingsExist() {
        when(bookingRepository.findAllByItemIdIn(anyList())).thenReturn(List.of(booking));
        List<Booking> result = bookingService.getAllByItemIdIn(List.of(1L, 2L));
        verify(bookingRepository, times(1)).findAllByItemIdIn(anyList());
        assertFalse(result.isEmpty());
    }

    @Test
    void getAllByItemIdInIfBookingsDoNotExist() {
        when(bookingRepository.findAllByItemIdIn(anyList())).thenReturn(List.of());
        List<Booking> result = bookingService.getAllByItemIdIn(List.of(1L, 2L));
        verify(bookingRepository, times(1)).findAllByItemIdIn(anyList());
        assertTrue(result.isEmpty());
    }

    @Test
    void getAllByItemIdAndTimeIfBookingsExist() {
        when(bookingRepository.findByItemIdAndEndIsBefore(anyLong(), any(LocalDateTime.class)))
                .thenReturn(List.of(booking));
        List<Booking> result = bookingService.getAllByItemIdAndTime(1L, LocalDateTime.now());
        verify(bookingRepository, times(1)).findByItemIdAndEndIsBefore(anyLong(), any());
        assertFalse(result.isEmpty());
    }

    @Test
    void getAllByItemIdAndTimeIfBookingsDoNotExist() {
        when(bookingRepository.findByItemIdAndEndIsBefore(anyLong(), any(LocalDateTime.class)))
                .thenReturn(List.of());
        List<Booking> result = bookingService.getAllByItemIdAndTime(1L, LocalDateTime.now());
        verify(bookingRepository, times(1)).findByItemIdAndEndIsBefore(anyLong(), any());
        assertTrue(result.isEmpty());
    }

    @Test
    void updateStatusIfBookingDoesNotExist() {
        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> bookingService.updateStatus(1L, true, 1L));
    }

    @Test
    void updateStatusIfUserIsNotOwner() {
        when(bookingRepository.findById(anyLong()))
                .thenReturn(Optional.of(booking));

        assertThrows(EntityNotFoundException.class, () -> bookingService.updateStatus(1L, true, 2L));
    }

    @Test
    void updateStatusIfStatusIsNotWaiting() {
        booking.setStatus(Status.REJECTED);

        when(bookingRepository.findById(anyLong()))
                .thenReturn(Optional.of(booking));

        assertThrows(InvalidRequestException.class, () -> bookingService.updateStatus(1L, true, 1L));
    }

    @Test
    void updateStatusShouldBeOkStatusApproved() {
        boolean approved = true;
        booking.setStatus(Status.WAITING);

        when(bookingRepository.findById(anyLong()))
                .thenReturn(Optional.of(booking));

        when(bookingRepository.save(any(Booking.class)))
                .thenReturn(booking);

        ResponseBookingDto bookingResponse = bookingService.updateStatus(1L, approved, 1L);

        verify(bookingRepository, times(1)).save(any(Booking.class));
        assertEquals(Status.APPROVED, bookingResponse.getStatus());
    }

    @Test
    void updateStatusShouldBeOkStatusRejected() {
        boolean approved = false;
        booking.setStatus(Status.WAITING);

        when(bookingRepository.findById(anyLong()))
                .thenReturn(Optional.of(booking));

        when(bookingRepository.save(any(Booking.class)))
                .thenReturn(booking);

        ResponseBookingDto bookingResponse = bookingService.updateStatus(1L, approved, 1L);

        verify(bookingRepository, times(1)).save(any(Booking.class));
        assertEquals(Status.REJECTED, bookingResponse.getStatus());
    }

    @Test
    void getBookingByIdIfBookingDoesNotExist() {
        when(bookingRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> bookingService.findById(1L, 1L));
    }

    @Test
    void getBookingByIdIfUserIsNotOwnerOrBooker() {
        when(bookingRepository.findById(anyLong()))
                .thenReturn(Optional.of(booking));

        assertThrows(EntityNotFoundException.class, () -> bookingService.findById(1L, 33L));
    }

    @Test
    void getBookingByIdShouldBeOk() {
        when(bookingRepository.findById(anyLong()))
                .thenReturn(Optional.of(booking));

        bookingService.findById(1L, 1L);

        verify(bookingRepository, times(1)).findById(anyLong());
    }

    @Test
    void getAllByBookerIfUserDoesNotExist() {
        doThrow(EntityNotFoundException.class)
                .when(userService).checkIfUserExists(anyLong());

        assertThrows(EntityNotFoundException.class, () -> bookingService.getAllByBooker(1L, "ALL", 0, 10));
    }

    @Test
    void getAllByBookerIfPageableNotCorrect() {
        int from = -1;
        int size = -1;
        doNothing()
                .when(userService).checkIfUserExists(anyLong());

        assertThrows(IllegalArgumentException.class, () -> bookingService.getAllByBooker(1L, "ALL", from, size));
    }

    @Test
    void getAllByBookerIfStateNotCorrect() {
        String state = "NONE";
        doNothing()
                .when(userService).checkIfUserExists(anyLong());

        assertThrows(InvalidRequestException.class, () -> bookingService.getAllByBooker(1L, state, 0, 10));
    }

    @Test
    void getAllByBookerIfStateAll() {
        String state = "ALL";
        int from = 0;
        int size = 10;
        int page = 0;

        doNothing()
                .when(userService).checkIfUserExists(anyLong());

        Pageable pageable = PageRequest.of(page, size, Sort.by("start").descending());

        bookingService.getAllByBooker(1L, state, from, size);

        verify(bookingRepository, times(1))
                .findAllByBookerId(1L, pageable);
    }

    @Test
    void getAllByBookerIfStateCurrent() {
        String state = "CURRENT";
        int from = 0;
        int size = 10;

        doNothing()
                .when(userService).checkIfUserExists(anyLong());

        bookingService.getAllByBooker(1L, state, from, size);

        verify(bookingRepository, times(1))
                .findByBookerIdAndStartIsBeforeAndEndIsAfter(anyLong(), any(LocalDateTime.class),
                        any(LocalDateTime.class), any(Pageable.class));
    }

    @Test
    void getAllByBookerIfStatePast() {
        String state = "PAST";
        int from = 0;
        int size = 10;

        doNothing()
                .when(userService).checkIfUserExists(anyLong());

        bookingService.getAllByBooker(1L, state, from, size);

        verify(bookingRepository, times(1))
                .findByBookerIdAndEndIsBefore(anyLong(), any(LocalDateTime.class), any(Pageable.class));
    }

    @Test
    void getAllByBookerIfStateFuture() {
        String state = "FUTURE";
        int from = 0;
        int size = 10;

        doNothing()
                .when(userService).checkIfUserExists(anyLong());

        bookingService.getAllByBooker(1L, state, from, size);

        verify(bookingRepository, times(1))
                .findByBookerIdAndStartIsAfter(anyLong(), any(LocalDateTime.class), any(Pageable.class));
    }

    @Test
    void getAllByBookerIfStateWaiting() {
        String state = "WAITING";
        int from = 0;
        int size = 10;

        doNothing()
                .when(userService).checkIfUserExists(anyLong());

        bookingService.getAllByBooker(1L, state, from, size);

        verify(bookingRepository, times(1))
                .findByBookerIdAndStartIsAfterAndStatusIs(anyLong(), any(LocalDateTime.class), any(Pageable.class),
                        any(Status.class));
    }

    @Test
    void getAllByBookerIfStateRejected() {
        String state = "REJECTED";
        int from = 0;
        int size = 10;

        doNothing()
                .when(userService).checkIfUserExists(anyLong());

        bookingService.getAllByBooker(1L, state, from, size);

        verify(bookingRepository, times(1))
                .findByBookerIdAndStartIsAfterAndStatusIs(anyLong(), any(LocalDateTime.class), any(Pageable.class),
                        any(Status.class));
    }

    @Test
    void getAllByOwnerIfUserDoesNotExist() {
        doThrow(EntityNotFoundException.class)
                .when(userService).findUserById(anyLong());

        assertThrows(EntityNotFoundException.class,
                () -> bookingService.getAllByOwner(1L, "ALL", 0, 10));
    }

    @Test
    void getAllByOwnerIfPageableParamsIncorrect() {
        int from = -1;
        int size = -1;

        when(userService.findUserById(anyLong())).thenReturn(user);

        assertThrows(IllegalArgumentException.class,
                () -> bookingService.getAllByOwner(1L, "ALL", from, size));
    }

    @Test
    void getAllByOwnerIfStateAll() {
        String state = "ALL";
        int from = 0;
        int size = 10;
        int page = 0;

        when(userService.findUserById(anyLong()))
                .thenReturn(user);

        when(itemRepository.findAllByOwnerId(anyLong()))
                .thenReturn(List.of(item));

        Pageable pageable = PageRequest.of(page, size, Sort.by("start").descending());

        bookingService.getAllByOwner(1L, state, from, size);

        verify(bookingRepository, times(1))
                .findAllByItemIdIn(List.of(item.getId()), pageable);
    }

    @Test
    void getAllByOwnerIfStateCurrent() {
        String state = "CURRENT";
        int from = 0;
        int size = 10;

        when(userService.findUserById(anyLong()))
                .thenReturn(user);

        when(itemRepository.findAllByOwnerId(anyLong()))
                .thenReturn(List.of(item));


        bookingService.getAllByOwner(1L, state, from, size);

        verify(bookingRepository, times(1))
                .findByItemIdInAndStartIsBeforeAndEndIsAfter(anyList(), any(LocalDateTime.class),
                        any(LocalDateTime.class), any(Pageable.class));
    }

    @Test
    void getAllByOwnerIfStatePast() {
        String state = "PAST";
        int from = 0;
        int size = 10;

        when(userService.findUserById(anyLong()))
                .thenReturn(user);

        when(itemRepository.findAllByOwnerId(anyLong()))
                .thenReturn(List.of(item));


        bookingService.getAllByOwner(1L, state, from, size);

        verify(bookingRepository, times(1))
                .findByItemIdInAndEndIsBefore(anyList(), any(LocalDateTime.class), any(Pageable.class));
    }

    @Test
    void getAllByOwnerIfStateFuture() {
        String state = "FUTURE";
        int from = 0;
        int size = 10;

        when(userService.findUserById(anyLong()))
                .thenReturn(user);

        when(itemRepository.findAllByOwnerId(anyLong()))
                .thenReturn(List.of(item));


        bookingService.getAllByOwner(1L, state, from, size);

        verify(bookingRepository, times(1))
                .findByItemIdInAndStartIsAfter(anyList(), any(LocalDateTime.class), any(Pageable.class));
    }

    @Test
    void getAllByOwnerIfStateWaiting() {
        String state = "WAITING";
        int from = 0;
        int size = 10;

        when(userService.findUserById(anyLong()))
                .thenReturn(user);

        when(itemRepository.findAllByOwnerId(anyLong()))
                .thenReturn(List.of(item));


        bookingService.getAllByOwner(1L, state, from, size);

        verify(bookingRepository, times(1))
                .findByItemIdInAndStartIsAfterAndStatusIs(anyList(), any(LocalDateTime.class), any(Pageable.class),
                        any(Status.class));
    }

    @Test
    void getAllByOwnerIfStateRejected() {
        String state = "REJECTED";
        int from = 0;
        int size = 10;

        when(userService.findUserById(anyLong()))
                .thenReturn(user);

        when(itemRepository.findAllByOwnerId(anyLong()))
                .thenReturn(List.of(item));


        bookingService.getAllByOwner(1L, state, from, size);

        verify(bookingRepository, times(1))
                .findByItemIdInAndStartIsAfterAndStatusIs(anyList(), any(LocalDateTime.class), any(Pageable.class),
                        any(Status.class));
    }
}
