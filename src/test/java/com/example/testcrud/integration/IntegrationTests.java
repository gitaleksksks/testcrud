package com.example.testcrud.integration;

import com.example.testcrud.config.SecurityConfig;
import com.example.testcrud.controller.UserController;
import com.example.testcrud.dto.UserDTO;
import com.example.testcrud.dto.UserDetailsDTO;
import com.example.testcrud.filter.JwtRequestFilter;
import com.example.testcrud.repository.UserRepository;
import com.example.testcrud.security.JwtUtil;
import com.example.testcrud.security.MyUserDetailsService;
import com.example.testcrud.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Import({
        SecurityConfig.class,
        JwtUtil.class,
        JwtRequestFilter.class,
        MyUserDetailsService.class,
        UserRepository.class,
        UserService.class,
        UserController.class
})
public class IntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserService userService;

    private UserDTO mockUserDTO;

    private UserDetailsDTO mockUserDetailsDTO;

    @BeforeEach
    void setUp() {
        mockUserDTO = new UserDTO();
        mockUserDTO.setLastName("Ivanov");
        mockUserDTO.setFirstName("Ivan");
        mockUserDTO.setEmail("ivan.ivanov@example.com");
        mockUserDTO.setBirthDate(LocalDate.of(1970, 1, 15));

        mockUserDetailsDTO = new UserDetailsDTO();
        mockUserDetailsDTO.setAdditionalInfo("Some additional information about the user");
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    @WithMockUser
    void testCreateAndGetUser() throws Exception {

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockUserDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.lastName").value(mockUserDTO.getLastName()))
                .andExpect(jsonPath("$.firstName").value(mockUserDTO.getFirstName()))
                .andExpect(jsonPath("$.email").value(mockUserDTO.getEmail()));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].lastName").value(mockUserDTO.getLastName()))
                .andExpect(jsonPath("$[0].firstName").value(mockUserDTO.getFirstName()))
                .andExpect(jsonPath("$[0].email").value(mockUserDTO.getEmail()));
    }

    @Test
    @WithMockUser
    void testCreateAndGetUserDetails() throws Exception {

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockUserDTO)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/user-details")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockUserDetailsDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.additionalInfo").value(mockUserDetailsDTO.getAdditionalInfo()));

        mockMvc.perform(get("/user-details"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].additionalInfo").value(mockUserDetailsDTO.getAdditionalInfo()));
    }

    @Test
    @WithMockUser
    void testUpdateUser() throws Exception {

        String response = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockUserDTO)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long userId = objectMapper.readTree(response).get("id").asLong();

        mockUserDTO.setFirstName("UpdatedFirstName");
        mockMvc.perform(put("/users/" + userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockUserDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("UpdatedFirstName"));
    }

    @Test
    @WithMockUser
    void testDeleteUser() throws Exception {

        String response = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockUserDTO)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long userId = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(delete("/users/" + userId))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/users/" + userId))
                .andExpect(status().isNotFound());
    }
}
