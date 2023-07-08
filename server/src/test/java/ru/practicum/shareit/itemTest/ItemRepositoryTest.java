package ru.practicum.shareit.itemTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;

@DataJpaTest
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
@Sql("classpath:tests/data.sql")
class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;

    @Test
    void shouldFindByTextIfItemExistsAssertName() {
        List<Item> results = itemRepository.findByText("лопата");
        assertFalse(results.isEmpty());
        assertThat(results.get(0).getName()).isEqualTo("Лопата");
    }

    @Test
    void shouldFindByTextIfItemExistsAssertOwnerId() {
        List<Item> results = itemRepository.findByText("лопата");
        assertFalse(results.isEmpty());
        assertThat(results.get(0).getOwner().getId()).isEqualTo(1L);
    }

    @Test
    void shouldFindByByOwnerIdIfItemsExistAssertName() {
        Page<Item> results = itemRepository.findAllByOwnerId(1L, Pageable.ofSize(1));
        assertFalse(results.isEmpty());
        Item item = new Item();
        item.setName("Лопата");
        assertThat(results.get().collect(Collectors.toList()).get(0).getName()).isEqualTo("Лопата");
    }

    @Test
    void shouldFindByByOwnerIdIfItemsExistAssertId() {
        Page<Item> results = itemRepository.findAllByOwnerId(1L, Pageable.ofSize(1));
        assertFalse(results.isEmpty());
        Item item = new Item();
        item.setName("Лопата");
        item.setId(1L);
        assertThat(results.get().collect(Collectors.toList()).get(0).getId()).isEqualTo(1L);
    }

    @Test
    void shouldFindByRequestIdInIfItemsExistAssertName() {
        List<Item> results = itemRepository.findAllByRequestIdIn(List.of(1L));
        assertFalse(results.isEmpty());
        assertThat(results.get(0).getName()).isEqualTo("Грабли");
    }

    @Test
    void shouldFindByRequestIdInIfItemsExistAssertId() {
        List<Item> results = itemRepository.findAllByRequestIdIn(List.of(1L));
        assertFalse(results.isEmpty());
        assertThat(results.get(0).getOwner().getId()).isEqualTo(2L);
    }

    @Test
    void shouldFindByRequestIdIfItemExistAssertName() {
        Item result = itemRepository.findByRequestId(1L);
        assertNotNull(result);
        assertThat(result.getName()).isEqualTo("Грабли");
    }

    @Test
    void shouldFindByRequestIdIfItemExistAssertId() {
        Item result = itemRepository.findByRequestId(1L);
        assertNotNull(result);
        assertThat(result.getOwner().getId()).isEqualTo(2L);
    }

}