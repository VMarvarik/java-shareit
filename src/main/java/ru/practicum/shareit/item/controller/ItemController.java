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
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

import static ru.practicum.shareit.request.controller.RequestController.REQUEST_HEADER;

@Validated
@RestController
@RequestMapping("/items")
@Slf4j
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @ResponseStatus(code = HttpStatus.CREATED)
    @PostMapping
    public ItemResponseDto addItem(@RequestHeader(REQUEST_HEADER) Long ownerId,
                                   @Valid @RequestBody ItemRequestDto itemRequestDto) {
        log.info("Добавление предмета");
        return itemService.addItem(itemRequestDto, ownerId);
    }

    @ResponseStatus(code = HttpStatus.OK)
    @GetMapping("/{id}")
    public ItemResponseDto getItem(@RequestHeader(REQUEST_HEADER) Long ownerId, @PathVariable Long id) {
        log.info("Вызов предмета");
        return itemService.getItem(id, ownerId);
    }

    @ResponseStatus(code = HttpStatus.OK)
    @GetMapping
    public List<ItemResponseDto> getOwnerItems(@RequestHeader(REQUEST_HEADER) Long ownerId,
                                               @RequestParam(value = "from",
                                                       defaultValue = "0", required = false) @PositiveOrZero Integer from,
                                               @RequestParam(value = "size",
                                                       defaultValue = "10", required = false) @Positive Integer size) {
        log.info("Поиск предметов по пользователю");
        return itemService.getOwnerItems(ownerId);
    }

    @ResponseStatus(code = HttpStatus.OK)
    @GetMapping("/search")
    public List<ItemResponseDto> searchAvailableItems(@RequestParam(name = "text") String text,
                                                      @RequestParam(value = "from",
                                                              defaultValue = "0", required = false) @PositiveOrZero Integer from,
                                                      @RequestParam(value = "size",
                                                              defaultValue = "10", required = false) @Positive Integer size) {
        log.info("Поиск предметов по тексту");
        return itemService.searchAvailableItems(text);
    }

    @ResponseStatus(code = HttpStatus.OK)
    @PatchMapping("/{itemId}")
    public ItemResponseDto updateItem(@RequestHeader(REQUEST_HEADER) Long ownerId, @PathVariable Long itemId,
                                      @RequestBody ItemRequestDto itemRequestDto) {
        log.info("Обновление предмета");
        return itemService.updateItem(itemRequestDto, itemId, ownerId);
    }

    @ResponseStatus(code = HttpStatus.OK)
    @PostMapping("/{itemId}/comment")
    public ResponseCommentDto addComment(@RequestHeader(REQUEST_HEADER) Long userId, @PathVariable Long itemId,
                                         @Valid @RequestBody RequestCommentDto request) {
        log.info("Добавление комментария");
        return itemService.addComment(request, userId, itemId);
    }
}
