package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.dto.RequestCommentDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Min;

import static ru.practicum.shareit.request.RequestController.USER_ID_HEADER;

@Validated
@RestController
@RequestMapping("/items")
@Slf4j
@RequiredArgsConstructor
public class ItemController {
    private final ItemClient itemClient;

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<Object> getOwnerItems(@RequestHeader(name = USER_ID_HEADER) Long userId,
                                                @RequestParam(defaultValue = "0") @Min(0) Integer from,
                                                @RequestParam(defaultValue = "10") @Min(1) Integer size) {
        log.info("Поиск предметов по пользователю");
        return itemClient.getOwnerItems(userId, from, size);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity<Object> addItem(@RequestHeader(name = USER_ID_HEADER) Long userId,
                                          @RequestBody @Valid ItemRequestDto request) {
        log.info("Добавление предмета");
        return itemClient.addItem(userId, request);
    }

    @GetMapping("/{itemId}")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<Object> getItem(@RequestHeader(name = USER_ID_HEADER) Long userId, @PathVariable Long itemId) {
        log.info("Вызов предмета");
        return itemClient.getItem(userId, itemId);
    }

    @PatchMapping("/{itemId}")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<Object> updateItem(@RequestHeader(USER_ID_HEADER) Long userId, @PathVariable Long itemId,
                                             @RequestBody ItemRequestDto request) {
        log.info("Обновление предмета");
        return itemClient.updateItem(userId, itemId, request);
    }

    @GetMapping("/search")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<Object> searchAvailableItems(@RequestHeader(name = USER_ID_HEADER) Long userId, @RequestParam String text,
                                                       @RequestParam(defaultValue = "0") @Min(0) Integer from,
                                                       @RequestParam(defaultValue = "10") @Min(1) Integer size) {
        log.info("Поиск предметов по тексту");
        return itemClient.searchAvailableItems(userId, text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<Object> addComment(@RequestHeader(name = USER_ID_HEADER) Long userId, @PathVariable Long itemId,
                                             @RequestBody @Valid RequestCommentDto request) {
        log.info("Добавление комментария");
        return itemClient.addComment(userId, itemId, request);
    }
}
