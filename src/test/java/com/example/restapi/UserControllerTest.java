package com.example.restapi;

import com.example.restapi.model.User;
import com.example.restapi.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private User savedUser;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        savedUser = userRepository.save(new User("John", "Doe", "john.doe@example.com", "555-1234"));
    }

    @Test
    void getAllUsers_returnsListOfUsers() throws Exception {
        userRepository.save(new User("Jane", "Doe", "jane.doe@example.com", "555-5678"));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].firstName", is("John")));
    }

    @Test
    void getUserById_existingId_returnsUser() throws Exception {
        mockMvc.perform(get("/api/users/{id}", savedUser.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("John")))
                .andExpect(jsonPath("$.lastName", is("Doe")))
                .andExpect(jsonPath("$.email", is("john.doe@example.com")));
    }

    @Test
    void getUserById_nonExistingId_returns404() throws Exception {
        mockMvc.perform(get("/api/users/{id}", 9999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("User not found with id: 9999")));
    }

    @Test
    void updateUser_validRequest_returnsUpdatedUser() throws Exception {
        User update = new User("Johnny", "Doe", "johnny.doe@example.com", "555-9999");

        mockMvc.perform(put("/api/users/{id}", savedUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Johnny")))
                .andExpect(jsonPath("$.email", is("johnny.doe@example.com")))
                .andExpect(jsonPath("$.phone", is("555-9999")));
    }

    @Test
    void updateUser_nonExistingId_returns404() throws Exception {
        User update = new User("Ghost", "User", "ghost@example.com", "000-0000");

        mockMvc.perform(put("/api/users/{id}", 9999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateUser_invalidEmail_returns400() throws Exception {
        User update = new User("John", "Doe", "not-an-email", "555-1234");

        mockMvc.perform(put("/api/users/{id}", savedUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors.email").exists());
    }
}
