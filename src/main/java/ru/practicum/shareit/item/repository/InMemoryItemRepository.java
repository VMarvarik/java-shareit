package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class InMemoryItemRepository implements ItemRepository {

    private final Map<Long, Item> itemMap = new HashMap<>();
    private long id = 1L;

    @Override
    public Optional<Item> addItem(Item item) {
        item.setItemId(id++);
        itemMap.put(item.getItemId(), item);
        return Optional.of(itemMap.get(item.getItemId()));
    }

    @Override
    public Optional<Item> getItem(long id) {
        if (itemMap.containsKey(id)) {
            return Optional.of(itemMap.get(id));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Collection<Item> getOwnerItems(long ownerId) {
        return itemMap.values().stream().filter(item -> item.getOwnerId() == ownerId)
                .sorted(Comparator.comparingLong(Item::getItemId))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Item> updateItem(Item item, long id) {
        Item itemUpdate = itemMap.get(id);
        Optional.ofNullable(item.getName()).ifPresent(itemUpdate::setName);
        Optional.ofNullable(item.getDescription()).ifPresent(itemUpdate::setDescription);
        Optional.ofNullable(item.getAvailable()).ifPresent(itemUpdate::setAvailable);
        return Optional.of(itemUpdate);
    }

    @Override
    public Collection<Item> searchAvailableItems(String query) {
        return itemMap.values()
                .stream()
                .filter(i -> (i.getName().toLowerCase().contains(query)
                        || i.getDescription().toLowerCase().contains(query)
                        && i.getAvailable()))
                .collect(Collectors.toList());
    }
}
