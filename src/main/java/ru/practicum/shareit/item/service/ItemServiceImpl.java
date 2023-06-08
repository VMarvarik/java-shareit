package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.comment.repository.CommentRepository;
import ru.practicum.shareit.exception.AccessDenied;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Transactional
    @Override
    public Item addItem(Item item, long ownerId) {
        User owner = userRepository.findById(ownerId).orElseThrow(() -> new EntityNotFoundException("Пользователь не найден."));
        return itemRepository.save(item);
    }

    @Transactional
    @Override
    public Item getItem(long id) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Вещь не найдена."));
        return itemRepository.getById(id);
    }

    @Transactional
    @Override
    public Collection<Item> getOwnerItems(long ownerId) {
        User owner = userRepository.findById(ownerId).orElseThrow(() -> new EntityNotFoundException("Пользователь не найден."));
        return itemRepository.findAllByOwnerIdOrderByIdAsc(ownerId);
    }

    @Transactional
    @Override
    public Item updateItem(Item item, long id, long ownerId) {
        Item itemCheck = itemRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Вещь не найдена."));
        if (itemCheck.getOwner().getId() != ownerId) {
            throw new AccessDenied("Пользователь не имеет прав на обовление этой вещи");
        }
        //List<Comment> comments = commentRepository.findAllByItemId(id);
        return itemRepository.save(itemCheck);
    }

    @Transactional
    @Override
    public Collection<Item> searchAvailableItems(String query) {
        if (!query.isEmpty()) {
            return itemRepository.searchItemsByStringIfAvailable(query.toLowerCase());
        }
        return Collections.emptyList();
    }

    @Transactional
    @Override
    public Comment addComment(long ownerId, long itemId, Comment comment) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new EntityNotFoundException("Вещи нет в базе."));
        User owner = userRepository.findById(ownerId).orElseThrow(() -> new EntityNotFoundException("Пользователь не найден."));
        List<Booking> bookings = bookingRepository.findAllByBookerIdAndItemIdAndEndBeforeOrderByStartDesc(owner.getId(), itemId, LocalDateTime.now());
        if (bookings.isEmpty()) {
            throw new AccessDenied("Нет прав на добавление комментария. Пользователь не бронировал вещь или бронь ещё не окончена.");
        } else {
            Comment commentAdded = new Comment(comment.getText(), item, owner, LocalDateTime.now());
            return commentRepository.save(commentAdded);
        }
    }

    @Transactional
    @Override
    public void deleteItem(long ownerId, long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(
                () -> new EntityNotFoundException("Вещи нет в базе.")
        );
        if (item.getOwner().getId() != ownerId) {
            throw new AccessDenied("Нет прав на удаление этой вещи. Вы не являетесь её владельцем.");
        } else {
            itemRepository.deleteById(itemId);
        }
    }
}
