package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.dto.RequestCommentDto;
import ru.practicum.shareit.comment.dto.ResponseCommentDto;
import ru.practicum.shareit.comment.mapper.CommentMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Validated
@RestController
@RequestMapping(path = "/items")
@Slf4j
@RequiredArgsConstructor
public class ItemController {

    private final ItemService service;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ItemDto addItem(@RequestBody @Valid ItemDto itemDto,
                           @RequestHeader(name = "X-Sharer-User-Id") final long ownerId) {
        Item item = ItemMapper.mapToModel(itemDto);
        log.info("Добавление предмета");
        return ItemMapper.mapToDto(service.addItem(ItemMapper.mapToModel(itemDto), ownerId));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/{id}")
    public ItemDto getItem(@PathVariable long id, @RequestHeader(name = "X-Sharer-User-Id") long ownerId) {
        log.info("Вызов предмета");
        return ItemMapper.mapToDto(service.getItem(id));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<ItemDto> getOwnerItems(@RequestHeader(name = "X-Sharer-User-Id") long ownerId) {
        log.info("Поиск предметов по пользователю");
        return service.getOwnerItems(ownerId).stream().map(ItemMapper::mapToDto).collect(Collectors.toList());
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/search")
    public List<ItemDto> searchAvailableItems(@RequestParam String text,
                                              @RequestHeader(name = "X-Sharer-User-Id") long ownerId) {
        log.info("Поиск предметов по тексту");
        return service.searchAvailableItems(text).stream().map(ItemMapper::mapToDto).collect(Collectors.toList());
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping(path = "/{itemId}")
    public ItemDto updateItem(@RequestBody ItemDto itemDto,
                              @PathVariable long itemId,
                              @RequestHeader(name = "X-Sharer-User-Id") long ownerId) {
        log.info("Обновление предмета");
        return ItemMapper.mapToDto(service.updateItem(ItemMapper.mapToModel(itemDto), itemId, ownerId));
    }

    @PostMapping("{itemId}/comment")
    public ResponseCommentDto addComment(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @PathVariable long itemId,
            @Valid @RequestBody RequestCommentDto requestCommentDto) {
        return CommentMapper.toResponseCommentDto(service.addComment(userId, itemId, CommentMapper.fromRequestCommentDtoToModel(requestCommentDto)));
    }
}
