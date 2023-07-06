package ru.practicum.shareit.item.service;

import ru.practicum.shareit.comment.dto.RequestCommentDto;
import ru.practicum.shareit.comment.dto.ResponseCommentDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    ItemResponseDto addItem(ItemRequestDto itemRequestDto, Long ownerId);

    ItemResponseDto getItem(Long id, Long userId);

    List<ItemResponseDto> getOwnerItems(Long ownerId);

    ItemResponseDto updateItem(ItemRequestDto itemRequestDto, Long id, Long ownerId);

    List<ItemResponseDto> searchAvailableItems(String query);

    ResponseCommentDto addComment(RequestCommentDto comment, Long ownerId, Long itemId);

    Item findByRequestId(Long id);

    List<Item> findAllByRequestIdIn(List<Long> ids);
}
