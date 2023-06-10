package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.dto.RequestCommentDto;
import ru.practicum.shareit.comment.dto.ResponseCommentDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@RequestMapping("/items")
@Slf4j
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ItemResponseDto addItem(@RequestHeader(name = "X-Sharer-User-Id") Long ownerId,
                                   @Valid @RequestBody ItemRequestDto itemRequestDto) {
        log.info("Добавление предмета");
        return itemService.addItem(itemRequestDto, ownerId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public ItemResponseDto getItem(@RequestHeader(name = "X-Sharer-User-Id") Long ownerId, @PathVariable Long id) {
        log.info("Вызов предмета");
        return itemService.getItem(id, ownerId);
    }

    @GetMapping
    public List<ItemResponseDto> getOwnerItems(@RequestHeader(name = "X-Sharer-User-Id") Long ownerId) {
        log.info("Поиск предметов по пользователю");
        return itemService.getOwnerItems(ownerId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/search")
    public List<ItemResponseDto> searchAvailableItems(@RequestParam(name = "text") String text) {
        log.info("Поиск предметов по тексту");
        return itemService.searchAvailableItems(text);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{itemId}")
    public ItemResponseDto updateItem(@RequestHeader(name = "X-Sharer-User-Id") Long ownerId, @PathVariable Long itemId,
                                      @RequestBody ItemRequestDto itemRequestDto) {
        log.info("Обновление предмета");
        return itemService.updateItem(itemRequestDto, itemId, ownerId);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/{itemId}/comment")
    public ResponseCommentDto addComment(@RequestHeader(name = "X-Sharer-User-Id") Long userId, @PathVariable Long itemId,
                                         @Valid @RequestBody RequestCommentDto request) {
        log.info("Добавление комментария");
        return itemService.addComment(request, userId, itemId);
    }
}
