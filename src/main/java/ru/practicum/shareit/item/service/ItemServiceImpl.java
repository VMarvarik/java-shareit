package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserService userService;

    @Override
    public Optional<Item> addItem(Item item, long ownerId) {
        userService.getUser(ownerId);
        item.setOwnerId(ownerId);
        return itemRepository.addItem(item);
    }

    @Override
    public Optional<Item> getItem(long id) {
        return Optional.of(itemRepository.getItem(id).orElseThrow(() ->
                new EntityNotFoundException(String.format("Ошибка получения: вещь с id=%d не найдена.", id))));
    }

    @Override
    public Collection<Item> getOwnerItems(long ownerId) {
        return itemRepository.getOwnerItems(ownerId);
    }

    @Override
    public Optional<Item> updateItem(Item item, long id, long ownerId) {
        itemRepository.getItem(id).map(Item::getOwnerId).filter(s -> s == ownerId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Ошибка получения: пользователь с id=%d не найден.", id)));
        return itemRepository.updateItem(item, id);
    }

    @Override
    public Collection<Item> searchAvailableItems(String query) {
        if (!query.isEmpty()) {
            return itemRepository.searchAvailableItems(query.toLowerCase());
        }
        return Collections.emptyList();
    }
}
