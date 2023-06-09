package ru.practicum.shareit.integrationTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.comment.dto.RequestCommentDto;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserDtoResponse;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemServiceIntegrationTest {
    private final ItemService itemService;
    private final UserService userService;

    public static ItemRequestDto createItemRequestDto(boolean available, Long requestId) {
        return ItemRequestDto.builder()
                .name("Предмет")
                .description("Описание")
                .available(available)
                .requestId(requestId)
                .build();
    }

    @Test
    void addItemsGetByUserIfUserNotExist() {
        ItemRequestDto itemRequest = createItemRequestDto(true, null);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> itemService.addItem(itemRequest, 22L));

        assertEquals("Пользователь не найден.", exception.getMessage());
    }

    @Test
    void addItemsGetByUser() {
        ItemRequestDto itemRequest = createItemRequestDto(true, null);

        UserDto userRequest1 = UserDto.builder().name("test1").email("test1@test.com").build();
        UserDto userRequest2 = UserDto.builder().name("test2").email("test2@test.com").build();

        UserDtoResponse user1 = userService.addUser(userRequest1);
        UserDtoResponse user2 = userService.addUser(userRequest2);

        itemService.addItem(itemRequest, user1.getId());
        itemService.addItem(itemRequest, user1.getId());

        itemService.addItem(itemRequest, user2.getId());

        List<ItemResponseDto> results = itemService.getOwnerItems(user1.getId());

        assertThat(results).hasSize(2);
    }

    @Test
    void addCommentIfUserExistsItemNotExists() {
        UserDto userRequest1 = UserDto.builder().name("test name").email("test@test.com").build();
        UserDtoResponse user = userService.addUser(userRequest1);
        RequestCommentDto commentRequest = RequestCommentDto.builder()
                .text("комментарий")
                .build();

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> itemService.addComment(commentRequest, user.getId(), 100L));

        assertEquals("Вещь не найдена.", exception.getMessage());
    }
}
