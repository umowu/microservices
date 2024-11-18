package ru.itmentor.spring.boot_security.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.itmentor.spring.boot_security.demo.models.User;
import ru.itmentor.spring.boot_security.demo.services.UserService;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void cleanDatabase() {
        userService.findAll().forEach(user -> userService.deleteUser(user.getId()));
    }

    @Test
    void testFindAllUsers() {
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("password");
        userService.save(user);

        List<User> users = userService.findAll();
        assertThat(users).hasSize(1);
        assertThat(users.get(0).getUsername()).isEqualTo("testUser");
    }

    @Test
    void testFindUserById() {
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("password");
        userService.save(user);

        User foundUser = userService.findById(user.getId());
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getUsername()).isEqualTo("testUser");
    }

    @Test
    void testFindUserByUsername() {
        User user = new User();
        user.setUsername("uniqueUser");
        user.setPassword("password");
        userService.save(user);

        Optional<User> foundUser = userService.findByUsername("uniqueUser");
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUsername()).isEqualTo("uniqueUser");
    }

    @Test
    void testSaveUserWithEncodedPassword() {
        User user = new User();
        user.setUsername("secureUser");
        user.setPassword("plainPassword");
        userService.save(user);

        User savedUser = userService.findById(user.getId());
        assertThat(savedUser).isNotNull();
        assertThat(passwordEncoder.matches("plainPassword", savedUser.getPassword())).isTrue();
    }

    @Test
    void testDeleteUser() {
        User user = new User();
        user.setUsername("toDelete");
        user.setPassword("password");
        userService.save(user);

        userService.deleteUser(user.getId());
        User deletedUser = userService.findById(user.getId());
        assertThat(deletedUser).isNull();
    }
}