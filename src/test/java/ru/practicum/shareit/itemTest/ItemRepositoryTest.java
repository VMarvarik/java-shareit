package ru.practicum.shareit.itemTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;

import java.util.List;

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
    void shouldFindByTextIfItemExists() {
        List<Item> results = itemRepository.findByText("лопата");

        assertFalse(results.isEmpty());
        assertThat(results.get(0).getName()).isEqualTo("Лопата");
        assertThat(results.get(0).getOwner().getId()).isEqualTo(1L);
    }

    @Test
    void shouldFindByByOwnerIdIfItemsExist() {
        List<Item> results = itemRepository.findAllByOwnerId(1L);

        assertFalse(results.isEmpty());
        assertThat(results.get(0).getName()).isEqualTo("Лопата");
        assertThat(results.get(0).getOwner().getId()).isEqualTo(1L);
    }

    @Test
    void shouldFindByRequestIdInIfItemsExist() {
        List<Item> results = itemRepository.findAllByRequestIdIn(List.of(1L));

        assertFalse(results.isEmpty());
        assertThat(results.get(0).getName()).isEqualTo("Грабли");
        assertThat(results.get(0).getOwner().getId()).isEqualTo(2L);
    }

    @Test
    void shouldFindByRequestIdIfItemExist() {
        Item result = itemRepository.findByRequestId(1L);

        assertNotNull(result);
        assertThat(result.getName()).isEqualTo("Грабли");
        assertThat(result.getOwner().getId()).isEqualTo(2L);
    }

}
