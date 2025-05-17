package com.example.testcrud.controller;

import com.example.testcrud.config.SecurityConfig;
import com.example.testcrud.dto.UserDTO;
import com.example.testcrud.filter.JwtRequestFilter;
import com.example.testcrud.repository.UserRepository;
import com.example.testcrud.security.JwtUtil;
import com.example.testcrud.security.MyUserDetailsService;
import com.example.testcrud.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@Import({SecurityConfig.class, MyUserDetailsService.class, JwtRequestFilter.class, JwtUtil.class})
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private UserDTO mockUserDTO;

    @BeforeEach
    void setUp() {
        mockUserDTO = new UserDTO();
        mockUserDTO.setId(1L);
        mockUserDTO.setLastName("Ivanov");
        mockUserDTO.setFirstName("Ivan");
        mockUserDTO.setEmail("ivan.ivanov@example.com");
        mockUserDTO.setBirthDate(LocalDate.of(1970, 1, 15));
    }

    @Test
    @WithMockUser
    void testGetAllUsers() throws Exception {
        when(userService.getAllUsers()).thenReturn(Collections.singletonList(mockUserDTO));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(mockUserDTO.getId()))
                .andExpect(jsonPath("$[0].lastName").value(mockUserDTO.getLastName()))
                .andExpect(jsonPath("$[0].firstName").value(mockUserDTO.getFirstName()))
                .andExpect(jsonPath("$[0].email").value(mockUserDTO.getEmail()));

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    @WithMockUser
    void testGetUserById() throws Exception {
        when(userService.getUserById(1L)).thenReturn(Optional.ofNullable(mockUserDTO));

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(mockUserDTO.getId()))
                .andExpect(jsonPath("$.lastName").value(mockUserDTO.getLastName()))
                .andExpect(jsonPath("$.firstName").value(mockUserDTO.getFirstName()))
                .andExpect(jsonPath("$.email").value(mockUserDTO.getEmail()));

        verify(userService, times(1)).getUserById(1L);
    }

    @Test
    @WithMockUser
    void testCreateUser() throws Exception {
        when(userService.createUser(any(UserDTO.class))).thenReturn(mockUserDTO);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockUserDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(mockUserDTO.getId()))
                .andExpect(jsonPath("$.lastName").value(mockUserDTO.getLastName()))
                .andExpect(jsonPath("$.firstName").value(mockUserDTO.getFirstName()))
                .andExpect(jsonPath("$.email").value(mockUserDTO.getEmail()));

        verify(userService, times(1)).createUser(any(UserDTO.class));
    }

    @Test
    @WithMockUser
    void testUpdateUser() throws Exception {
        when(userService.updateUser(eq(1L), any(UserDTO.class))).thenReturn(mockUserDTO);

        mockMvc.perform(put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockUserDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(mockUserDTO.getId()))
                .andExpect(jsonPath("$.lastName").value(mockUserDTO.getLastName()))
                .andExpect(jsonPath("$.firstName").value(mockUserDTO.getFirstName()))
                .andExpect(jsonPath("$.email").value(mockUserDTO.getEmail()));

        verify(userService, times(1)).updateUser(eq(1L), any(UserDTO.class));
    }

    @Test
    @WithMockUser
    void testDeleteUser() throws Exception {
        doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).deleteUser(1L);
    }
}
