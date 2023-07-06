package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.comment.dto.RequestCommentDto;
import ru.practicum.shareit.comment.dto.ResponseCommentDto;
import ru.practicum.shareit.comment.mapper.CommentMapper;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.comment.service.CommentService;
import ru.practicum.shareit.exception.AccessDenied;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.InvalidRequestException;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserService userService;
    private final BookingService bookingService;
    private final CommentService commentService;
    private final RequestRepository requestRepository;

    @Override
    @Transactional
    public ItemResponseDto addItem(ItemRequestDto itemRequestDto, Long ownerId) {
        User user = userService.findUserById(ownerId);

        Item item = ItemMapper.mapToModel(itemRequestDto);
        item.setOwner(user);
        Long requestId = itemRequestDto.getRequestId();
        if (itemRequestDto.getRequestId() != null) {
            Request request = requestRepository.findById(requestId)
                    .orElseThrow(() -> new EntityNotFoundException("Такого запроса нет"));
            item.setRequest(request);
        }
        Item savedItem = itemRepository.save(item);
        return ItemMapper.mapToDto(savedItem);
    }

    @Override
    @Transactional(readOnly = true)
    public ItemResponseDto getItem(Long itemId, Long userId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Вещь не найдена."));

        ItemResponseDto itemResponseDto = ItemMapper.mapToDto(item);

        if (Objects.equals(userId, item.getOwner().getId())) {
            List<Booking> bookingList = bookingService.getAllByItemId(itemId);
            setLastAndNextBookings(bookingList, itemResponseDto);
        }

        List<ResponseCommentDto> comments = commentService.getAllByItemId(itemId);
        itemResponseDto.setComments(comments);

        return itemResponseDto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemResponseDto> getOwnerItems(Long ownerId) {
        userService.checkIfUserExists(ownerId);

        List<Item> items = itemRepository.findAllByOwnerId(ownerId);
        List<ItemResponseDto> dtos = ItemMapper.mapToDto(items);

        List<Long> itemsId = dtos.stream()
                .map(ItemResponseDto::getId)
                .collect(Collectors.toList());

        List<Booking> bookingList = bookingService.getAllByItemIdIn(itemsId);

        return dtos
                .stream()
                .map(itemsDto -> setLastAndNextBookings(bookingList, itemsDto))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ItemResponseDto updateItem(ItemRequestDto request, Long itemId, Long ownerId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Вещь не найдена."));

        if (!Objects.equals(item.getOwner().getId(), ownerId)) {
            throw new AccessDenied("У пользователя нет доступа");
        }

        if (StringUtils.hasLength(request.getName())) {
            item.setName(request.getName());
        }

        if (StringUtils.hasLength(request.getDescription())) {
            item.setDescription(request.getDescription());
        }

        if ((request.getAvailable() != null)) {
            item.setAvailable(request.getAvailable());
        }

        Item savedItem = itemRepository.save(item);
        return ItemMapper.mapToDto(savedItem);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemResponseDto> searchAvailableItems(String query) {
        if (!StringUtils.hasLength(query)) {
            return List.of();
        }
        return ItemMapper.mapToDto(itemRepository.findByText(query));
    }

    @Override
    @Transactional
    public ResponseCommentDto addComment(RequestCommentDto request, Long ownerId, Long itemId) {
        Comment comment = CommentMapper.mapToModel(request);

        User author = userService.findUserById(ownerId);
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Вещь не найдена."));

        List<Booking> bookings = bookingService.getAllByItemIdAndTime(itemId, comment.getCreated())
                .stream()
                .filter(booking -> Objects.equals(booking.getBooker().getId(), ownerId))
                .collect(Collectors.toList());

        if (bookings.isEmpty()) {
            throw new InvalidRequestException("Нет брони для возможности добавления комментария");
        }

        comment.setAuthor(author);
        comment.setItem(item);
        Comment savedComment = commentService.save(comment);

        return CommentMapper.mapToDto(savedComment);
    }

    @Override
    public Item findByRequestId(Long id) {
        return itemRepository.findByRequestId(id);
    }

    @Override
    public List<Item> findAllByRequestIdIn(List<Long> ids) {
        return itemRepository.findAllByRequestIdIn(ids);
    }

    private ItemResponseDto setLastAndNextBookings(List<Booking> bookingList, ItemResponseDto itemResponseDto) {
        LocalDateTime dateTime = LocalDateTime.now();

        bookingList
                .stream()
                .filter(booking -> Objects.equals(booking.getItem().getId(), itemResponseDto.getId()))
                .sorted(Comparator.comparing(Booking::getEnd).reversed())
                .filter(booking -> booking.getStatus().equals(Status.APPROVED))
                .filter(booking -> booking.getStart().isBefore(dateTime))
                .limit(1)
                .findAny()
                .ifPresent(booking -> itemResponseDto.setLastBooking(BookingDto.builder()
                        .id(booking.getId())
                        .bookerId(booking.getBooker().getId())
                        .build()));

        bookingList
                .stream()
                .filter(booking -> Objects.equals(booking.getItem().getId(), itemResponseDto.getId()))
                .sorted(Comparator.comparing(Booking::getStart))
                .filter(booking -> booking.getStatus().equals(Status.APPROVED))
                .filter(booking -> booking.getStart().isAfter(dateTime))
                .limit(1)
                .findAny()
                .ifPresent(booking -> itemResponseDto.setNextBooking(BookingDto.builder()
                        .id(booking.getId())
                        .bookerId(booking.getBooker().getId())
                        .build()));

        return itemResponseDto;
    }
}
