package ru.practicum.shareit.itemTest;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;

@DataJpaTest
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
@Sql("classpath:tests/data.sql")
class ItemRepositoryTest {

//    @Autowired
//    ItemRepository itemRepository;
//
//    @Test
//    void shouldFindByTextIfItemExistsAssertName() {
//        List<Item> results = itemRepository.findByText("лопата");
//        assertFalse(results.isEmpty());
//        assertThat(results.get(0).getName()).isEqualTo("Лопата");
//    }
//
//    @Test
//    void shouldFindByTextIfItemExistsAssertOwnerId() {
//        List<Item> results = itemRepository.findByText("лопата");
//        assertFalse(results.isEmpty());
//        assertThat(results.get(0).getOwner().getId()).isEqualTo(1L);
//    }
//
//    @Test
//    void shouldFindByByOwnerIdIfItemsExistAssertName() {
//        List<Item> results = itemRepository.findAllByOwnerId(1L, Pageable.unpaged());
//        assertFalse(results.isEmpty());
//        assertThat(results.get(0).getName()).isEqualTo("Лопата");
//    }
//
//    @Test
//    void shouldFindByByOwnerIdIfItemsExistAssertId() {
//        List<Item> results = itemRepository.findAllByOwnerId(1L, Pageable.unpaged());
//        assertFalse(results.isEmpty());
//        assertThat(results.get(0).getOwner().getId()).isEqualTo(1L);
//    }
//
//    @Test
//    void shouldFindByRequestIdInIfItemsExistAssertName() {
//        List<Item> results = itemRepository.findAllByRequestIdIn(List.of(1L));
//        assertFalse(results.isEmpty());
//        assertThat(results.get(0).getName()).isEqualTo("Грабли");
//    }
//
//    @Test
//    void shouldFindByRequestIdInIfItemsExistAssertId() {
//        List<Item> results = itemRepository.findAllByRequestIdIn(List.of(1L));
//        assertFalse(results.isEmpty());
//        assertThat(results.get(0).getOwner().getId()).isEqualTo(2L);
//    }
//
//    @Test
//    void shouldFindByRequestIdIfItemExistAssertName() {
//        Item result = itemRepository.findByRequestId(1L);
//        assertNotNull(result);
//        assertThat(result.getName()).isEqualTo("Грабли");
//    }
//
//    @Test
//    void shouldFindByRequestIdIfItemExistAssertId() {
//        Item result = itemRepository.findByRequestId(1L);
//        assertNotNull(result);
//        assertThat(result.getOwner().getId()).isEqualTo(2L);
//    }

}
