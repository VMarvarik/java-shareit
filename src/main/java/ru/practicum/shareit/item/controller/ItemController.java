package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Optional;
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
    public Optional<ItemDto> addItem(@RequestBody @Valid final ItemDto itemDto,
                                     @RequestHeader(name = "X-Sharer-User-Id") final long ownerId) {
        Item item = ItemMapper.mapToModel(itemDto);
        item.setOwnerId(ownerId);
        log.info("Добавление предмета");
        return service.addItem(ItemMapper.mapToModel(itemDto), ownerId).map(ItemMapper::mapToDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/{id}")
    public Optional<ItemDto> getItem(@PathVariable final long id, @RequestHeader(name = "X-Sharer-User-Id") final long ownerId) {
        log.info("Вызов предмета");
        return service.getItem(id).map(ItemMapper::mapToDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public Collection<ItemDto> getOwnerItems(@RequestHeader(name = "X-Sharer-User-Id") final long ownerId) {
        log.info("Поиск предметов по пользователю");
        return service.getOwnerItems(ownerId).stream().map(ItemMapper::mapToDto).collect(Collectors.toList());
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/search")
    public Collection<ItemDto> searchAvailableItems(@RequestParam final String text,
                                                    @RequestHeader(name = "X-Sharer-User-Id") final long ownerId) {
        log.info("Поиск предметов по тексту");
        return service.searchAvailableItems(text).stream().map(ItemMapper::mapToDto).collect(Collectors.toList());
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping(path = "/{itemId}")
    public Optional<ItemDto> updateItem(@RequestBody final ItemDto itemDto,
                                        @PathVariable final long itemId,
                                        @RequestHeader(name = "X-Sharer-User-Id") final long ownerId) {
        log.info("Обновление предмета");
        return service.updateItem(ItemMapper.mapToModel(itemDto), itemId, ownerId).map(ItemMapper::mapToDto);
    }
}