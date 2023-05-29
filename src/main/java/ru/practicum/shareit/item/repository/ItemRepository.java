package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.Optional;

public interface ItemRepository {

    Optional<Item> addItem(Item item);

    Optional<Item> getItem(long id);

    Collection<Item> getOwnerItems(long ownerId);

    Optional<Item> updateItem(Item item, long id);

    Collection<Item> searchAvailableItems(String query);
}
