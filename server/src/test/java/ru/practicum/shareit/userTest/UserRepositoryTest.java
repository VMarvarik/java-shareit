package ru.practicum.shareit.userTest;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;

@DataJpaTest
@Sql("classpath:tests/data.sql")
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
class UserRepositoryTest {
//    @Autowired
//    UserRepository userRepository;
//
//    @Test
//    void findByEmailOneAssertId() {
//        Optional<User> resultOptional = userRepository.findByEmail("varvara@gmail.com");
//        assertTrue(resultOptional.isPresent());
//        assertThat(resultOptional.get().getId()).isNotNull();
//    }
//
//    @Test
//    void findByEmailOneAssertName() {
//        Optional<User> resultOptional = userRepository.findByEmail("varvara@gmail.com");
//        assertTrue(resultOptional.isPresent());
//        assertThat(resultOptional.get().getName()).isEqualTo("Варвара");
//    }
//
//    @Test
//    void findByEmailOneAssertNameAssertEmail() {
//        Optional<User> resultOptional = userRepository.findByEmail("varvara@gmail.com");
//        assertTrue(resultOptional.isPresent());
//        assertThat(resultOptional.get().getEmail()).isEqualTo("varvara@gmail.com");
//    }
//
//    @Test
//    void findByEmailTwoAssertId() {
//        Optional<User> resultOptional = userRepository.findByEmail("semyon@gmail.com");
//        assertTrue(resultOptional.isPresent());
//        assertThat(resultOptional.get().getId()).isNotNull();
//    }
//
//    @Test
//    void findByEmailTwoAssertName() {
//        Optional<User> resultOptional = userRepository.findByEmail("semyon@gmail.com");
//        assertTrue(resultOptional.isPresent());
//        assertThat(resultOptional.get().getName()).isEqualTo("Семен");
//    }
//
//    @Test
//    void findByEmailTwoAssertEmail() {
//        Optional<User> resultOptional = userRepository.findByEmail("semyon@gmail.com");
//        assertTrue(resultOptional.isPresent());
//        assertThat(resultOptional.get().getEmail()).isEqualTo("semyon@gmail.com");
//    }
//
//    @Test
//    void findByEmailThreeAssertId() {
//        Optional<User> resultOptional = userRepository.findByEmail("michael@gmail.com");
//        assertTrue(resultOptional.isPresent());
//        assertThat(resultOptional.get().getId()).isNotNull();
//    }
//
//    @Test
//    void findByEmailThreeAssertName() {
//        Optional<User> resultOptional = userRepository.findByEmail("michael@gmail.com");
//        assertTrue(resultOptional.isPresent());
//        assertThat(resultOptional.get().getName()).isEqualTo("Михаил");
//    }
//
//    @Test
//    void findByEmailThreeEmail() {
//        Optional<User> resultOptional = userRepository.findByEmail("michael@gmail.com");
//        assertTrue(resultOptional.isPresent());
//        assertThat(resultOptional.get().getEmail()).isEqualTo("michael@gmail.com");
//    }

}
