package ru.practicum.shareit.item.service;

import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemService {

    Item addItem(Item item, long ownerId);

    Item getItem(long id);

    Collection<Item> getOwnerItems(long ownerId);

    Item updateItem(Item item, long id, long ownerId);

    Collection<Item> searchAvailableItems(String query);

    Comment addComment(long ownerId, long itemId, Comment comment);

    void deleteItem(long ownerId, long itemId);
}
