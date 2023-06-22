package ru.practicum.shareit.itemTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.comment.dto.RequestCommentDto;
import ru.practicum.shareit.comment.mapper.CommentMapper;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.comment.service.CommentService;
import ru.practicum.shareit.exception.AccessDenied;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.InvalidRequestException;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
class ItemServiceTest {
    @Autowired
    ItemServiceImpl itemService;

    @MockBean
    ItemRepository itemRepository;

    @MockBean
    BookingService bookingService;

    @MockBean
    UserService userService;

    @MockBean
    CommentService commentService;

    @MockBean
    RequestRepository requestRepository;

    User user1;
    User user2;
    User user3;
    Item item;
    Booking bookingUser2;
    Booking bookingUser3;
    Comment comment;

    @BeforeEach
    void beforeEach() {
        user1 = User.builder().id(1L).name("Варвара").email("varvara@gmail.com").build();
        user2 = User.builder().id(2L).name("Семен").email("semyon@gmail.com").build();
        user3 = User.builder().id(3L).name("Михаил").email("michael@gmail.com").build();

        item = Item.builder()
                .id(1L)
                .name("Лопата")
                .description("Лопата для сада")
                .owner(user1)
                .request(new Request(10L, "Мне нужна лопата", user2, LocalDateTime.now()))
                .available(true)
                .build();

        bookingUser2 = Booking.builder()
                .id(1L)
                .item(item)
                .start(LocalDateTime.now().minusDays(10))
                .end(LocalDateTime.now().minusDays(5))
                .booker(user2)
                .status(Status.APPROVED)
                .build();

        bookingUser3 = Booking.builder()
                .id(3L)
                .item(item)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(15))
                .booker(user3)
                .status(Status.APPROVED)
                .build();

        comment = Comment.builder()
                .id(1L)
                .text("хорошо")
                .author(user2)
                .item(item)
                .created(LocalDateTime.now().minusDays(2))
                .build();


    }

    @Test
    void addItemShouldBeOk() {
        ItemRequestDto request = ItemRequestDto.builder()
                .name("Лопата")
                .description("Лопата для сада")
                .available(true)
                .build();

        when(userService.findUserById(anyLong())).thenReturn(user1);
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        itemService.addItem(request, 1L);

        verify(itemRepository, times(1)).save(any(Item.class));
    }

    @Test
    void addItemIfUserNotExists() {
        ItemRequestDto request = ItemRequestDto.builder()
                .name("Лопата")
                .description("Лопата для сада")
                .available(true)
                .build();

        doThrow(EntityNotFoundException.class)
                .when(userService).findUserById(anyLong());

        assertThrows(EntityNotFoundException.class, () -> itemService.addItem(request, 100L));
    }

    @Test
    void addItemShouldBeOkWithNullRequest() {
        ItemRequestDto request = ItemRequestDto.builder()
                .name("Лопата")
                .description("Лопата для сада")
                .available(true)
                .requestId(55L)
                .build();

        item.setRequest(null);

        when(userService.findUserById(anyLong())).thenReturn(user1);
        when(requestRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        ItemResponseDto result = itemService.addItem(request, 1L);

        verify(itemRepository, times(1)).save(any(Item.class));
        assertNull(result.getRequestId());
    }

    @Test
    void addItemShouldBeOkWithRequest() {
        Request ir = Request.builder()
                .id(10L)
                .description("Мне нужна лопатам")
                .build();
        ItemRequestDto request = ItemRequestDto.builder()
                .name("Лопата")
                .description("Лопата для сада")
                .available(true)
                .requestId(10L)
                .build();

        when(userService.findUserById(anyLong())).thenReturn(user1);
        when(requestRepository.findById(anyLong())).thenReturn(Optional.of(ir));
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        ItemResponseDto result = itemService.addItem(request, 1L);

        verify(itemRepository, times(1)).save(any(Item.class));
        assertEquals(10L, result.getRequestId());
    }

    @Test
    void updateItemByIdIfItemNotExists() {
        ItemRequestDto request = ItemRequestDto.builder()
                .name("Лопата новая")
                .description("Лопата для сада")
                .available(true)
                .build();

        doThrow(EntityNotFoundException.class)
                .when(itemRepository).findById(anyLong());

        assertThrows(EntityNotFoundException.class, () -> itemService.updateItem(request, 1L, 1L));
    }

    @Test
    void updateItemByIdIfUserIsNotOwnerOfItem() {
        ItemRequestDto request = ItemRequestDto.builder()
                .name("Лопата новая")
                .description("Лопата для сада")
                .available(true)
                .build();

        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(item));

        assertThrows(AccessDenied.class, () -> itemService.updateItem(request, 1L, 2L));
    }

    @Test
    void updateItemByIdIfUserExist() {
        ItemRequestDto request = ItemRequestDto.builder()
                .name("Лопата новая")
                .description("Лопата для сада")
                .available(true)
                .build();

        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(item));

        item.setName(request.getName());

        when(itemRepository.save(any(Item.class))).thenReturn(item);

        itemService.updateItem(request, 1L, 1L);

        verify(itemRepository, times(1)).save(any(Item.class));
    }

    @Test
    void getItemByIdIfItemNotExists() {
        doThrow(EntityNotFoundException.class)
                .when(itemRepository).findById(anyLong());

        assertThrows(EntityNotFoundException.class, () -> itemService.getItem(1L, 1L));
    }

    @Test
    void getItemByIdIfItemExists() {
        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(item));

        when(bookingService.getAllByItemId(anyLong()))
                .thenReturn(List.of(bookingUser2));

        when(commentService.getAllByItemId(anyLong()))
                .thenReturn(CommentMapper.mapToDtoList(List.of(comment)));

        itemService.getItem(1L, 1L);

        verify(itemRepository, times(1)).findById(anyLong());
    }

    @Test
    void getItemByIdIfItemExistsWithBookings() {
        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(item));

        when(bookingService.getAllByItemId(anyLong()))
                .thenReturn(List.of(bookingUser2, bookingUser3));

        when(commentService.getAllByItemId(anyLong()))
                .thenReturn(CommentMapper.mapToDtoList(List.of(comment)));

        ItemResponseDto result = itemService.getItem(1L, 1L);

        verify(itemRepository, times(1)).findById(anyLong());
        assertEquals(bookingUser2.getBooker().getId(), result.getLastBooking().getBookerId());
        assertEquals(bookingUser3.getBooker().getId(), result.getNextBooking().getBookerId());
    }

    @Test
    void getAllItemsByUserIdIfUserNotExists() {
        doThrow(EntityNotFoundException.class)
                .when(userService).checkIfUserExists(anyLong());

        assertThrows(EntityNotFoundException.class, () -> itemService.getOwnerItems(1L));
    }

    @Test
    void getAllItemsByUserIdIfUserAndItemsExist() {
        doNothing()
                .when(userService)
                .checkIfUserExists(anyLong());

        when(itemRepository.findAllByOwnerId(anyLong()))
                .thenReturn(List.of(item));

        when(bookingService.getAllByItemId(anyLong()))
                .thenReturn(List.of(bookingUser2));

        itemService.getOwnerItems(1L);

        verify(itemRepository, times(1)).findAllByOwnerId(anyLong());
    }

    @Test
    void getAllByRequestIdIfRequestsDoNotExist() {
        when(itemRepository.findAllByRequestIdIn(any()))
                .thenReturn(List.of());

        itemService.findAllByRequestIdIn(List.of(11L, 22L));

        verify(itemRepository, times(1)).findAllByRequestIdIn(any());
    }

    @Test
    void searchByQueryIfItemExist() {
        String text = "лопата";

        when(itemRepository.findByText(anyString()))
                .thenReturn(List.of(item));

        List<ItemResponseDto> result = itemService.searchAvailableItems(text);


        verify(itemRepository, times(1)).findByText(anyString());

        assertFalse(result.isEmpty());
    }

    @Test
    void searchByEmptyQueryIfItemExist() {
        String text = "";

        List<ItemResponseDto> result = itemService.searchAvailableItems(text);

        verify(itemRepository, times(0)).findByText(anyString());

        assertTrue(result.isEmpty());
    }

    @Test
    void addCommentIfUserNotExist() {
        RequestCommentDto request = RequestCommentDto.builder()
                .text("хорошо")
                .build();

        doThrow(EntityNotFoundException.class)
                .when(userService).findUserById(anyLong());

        assertThrows(EntityNotFoundException.class, () -> itemService.addComment(request, 2L, 1L));
    }

    @Test
    void addCommentIfItemNotExist() {
        RequestCommentDto request = RequestCommentDto.builder()
                .text("хорошо")
                .build();

        when(userService.findUserById(anyLong()))
                .thenReturn(user2);

        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.empty());


        assertThrows(EntityNotFoundException.class, () -> itemService.addComment(request, 2L, 2L));
    }

    @Test
    void addCommentIfIfUserHasNotBookingItem() {
        RequestCommentDto request = RequestCommentDto.builder()
                .text("хорошо")
                .build();

        when(userService.findUserById(anyLong()))
                .thenReturn(user2);

        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(item));

        when(bookingService.getAllByItemIdAndTime(anyLong(), any()))
                .thenReturn(List.of());

        assertThrows(InvalidRequestException.class, () -> itemService.addComment(request, 2L, 2L));
    }

    @Test
    void addCommentIfRequestIsCorrect() {
        RequestCommentDto request = RequestCommentDto.builder()
                .text("хорошо")
                .build();

        when(userService.findUserById(anyLong()))
                .thenReturn(user2);

        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(item));

        when(bookingService.getAllByItemIdAndTime(anyLong(), any()))
                .thenReturn(List.of(bookingUser2));

        when(commentService.save(any(Comment.class)))
                .thenReturn(comment);

        itemService.addComment(request, 2L, 2L);

        verify(commentService, times(1)).save(any(Comment.class));
    }
}