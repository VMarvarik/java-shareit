package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.Optional;

public interface ItemService {

    Optional<Item> addItem(Item item, long ownerId);

    Optional<Item> getItem(long id);

    Collection<Item> getOwnerItems(long ownerId);

    Optional<Item> updateItem(Item item, long id, long ownerId);

    Collection<Item> searchAvailableItems(String query);
}
