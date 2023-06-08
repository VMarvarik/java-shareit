package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    Collection<Item> findAllByOwnerIdOrderByIdAsc(long ownerId);

    @Query("SELECT i FROM Item i WHERE ((LOWER(i.name) LIKE %?1% " +
            "OR LOWER(i.description) LIKE %?1%) AND i.available = TRUE)")
    Collection<Item> searchItemsByStringIfAvailable(String searchString);
}
