package ru.itmentor.spring.boot_security.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.itmentor.spring.boot_security.demo.models.User;
import ru.itmentor.spring.boot_security.demo.repositories.UserRepository;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserRestControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testGetAllUsers() throws Exception {
        mockMvc.perform(get("/api/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", greaterThanOrEqualTo(0)));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testGetUserById() throws Exception {
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("password");
        userRepository.save(user);

        mockMvc.perform(get("/api/users/{id}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testUser"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testCreateUser() throws Exception {
        String newUserJson = """
                {
                  "username": "newUser",
                  "password": "password"
                }
                """;

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newUserJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("newUser"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testUpdateUser() throws Exception {
        User user = new User();
        user.setUsername("oldUser");
        user.setPassword("password");
        userRepository.save(user);

        String updatedUserJson = """
                {
                  "username": "updatedUser",
                  "password": "newPassword"
                }
                """;

        mockMvc.perform(put("/api/users/{id}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedUserJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("updatedUser"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testDeleteUser() throws Exception {
        User user = new User();
        user.setUsername("toDelete");
        user.setPassword("password");
        userRepository.save(user);

        mockMvc.perform(delete("/api/users/{id}", user.getId()))
                .andExpect(status().isNoContent());
    }
}
