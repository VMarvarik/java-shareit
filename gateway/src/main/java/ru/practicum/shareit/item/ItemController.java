package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.RequestCommentDto;
import ru.practicum.shareit.item.dto.ItemDtoForRequest;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import static ru.practicum.shareit.request.RequestController.REQUEST_HEADER;

@Validated
@RestController
@RequestMapping("/items")
@Slf4j
@RequiredArgsConstructor
public class ItemController {
    private final ItemClient itemClient;


    @ResponseStatus(code = HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<Object> addItem(@RequestHeader(REQUEST_HEADER) Long ownerId,
                                          @Valid @RequestBody ItemDtoForRequest itemDtoForRequest) {
        log.info("Добавление предмета");
        return itemClient.addItem(ownerId, itemDtoForRequest);
    }

    @ResponseStatus(code = HttpStatus.OK)
    @GetMapping("/{id}")
    public ResponseEntity<Object> getItem(@RequestHeader(REQUEST_HEADER) Long ownerId, @PathVariable Long id) {
        log.info("Вызов предмета");
        return itemClient.getItem(ownerId, id);
    }

    @ResponseStatus(code = HttpStatus.OK)
    @GetMapping
    public ResponseEntity<Object> getOwnerItems(@RequestHeader(REQUEST_HEADER) Long ownerId,
                                                @RequestParam(value = "from",
                                                        defaultValue = "0", required = false) @PositiveOrZero Integer from,
                                                @RequestParam(value = "size",
                                                        defaultValue = "10", required = false) @Positive Integer size) {
        log.info("Поиск предметов по пользователю");
        return itemClient.getOwnerItems(ownerId, from, size);
    }

    @ResponseStatus(code = HttpStatus.OK)
    @GetMapping("/search")
    public ResponseEntity<Object> searchAvailableItems(@RequestHeader(REQUEST_HEADER) Long userId,
                                                       @RequestParam(name = "text") String text,
                                                       @RequestParam(value = "from",
                                                               defaultValue = "0", required = false) @PositiveOrZero Integer from,
                                                       @RequestParam(value = "size",
                                                               defaultValue = "10", required = false) @Positive Integer size) {
        log.info("Поиск предметов по тексту");
        return itemClient.searchAvailableItems(userId, text, from, size);
    }

    @ResponseStatus(code = HttpStatus.OK)
    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestHeader(REQUEST_HEADER) Long ownerId, @PathVariable Long itemId,
                                             @RequestBody ItemDtoForRequest itemDtoForRequest) {
        log.info("Обновление предмета");
        return itemClient.updateItem(ownerId, itemId, itemDtoForRequest);
    }

    @ResponseStatus(code = HttpStatus.OK)
    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader(REQUEST_HEADER) Long userId, @PathVariable Long itemId,
                                             @Valid @RequestBody RequestCommentDto request) {
        log.info("Добавление комментария");
        return itemClient.addComment(userId, itemId, request);
    }
}
