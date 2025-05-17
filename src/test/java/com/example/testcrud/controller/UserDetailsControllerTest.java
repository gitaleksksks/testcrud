package com.example.testcrud.controller;

import com.example.testcrud.config.SecurityConfig;
import com.example.testcrud.dto.UserDetailsDTO;
import com.example.testcrud.filter.JwtRequestFilter;
import com.example.testcrud.repository.UserRepository;
import com.example.testcrud.security.JwtUtil;
import com.example.testcrud.security.MyUserDetailsService;
import com.example.testcrud.service.UserDetailsService;
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

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserDetailsController.class)
@Import({SecurityConfig.class, MyUserDetailsService.class, JwtRequestFilter.class, JwtUtil.class})
public class UserDetailsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private UserDetailsDTO mockUserDetailsDTO;

    @BeforeEach
    void setUp() {
        mockUserDetailsDTO = new UserDetailsDTO();
        mockUserDetailsDTO.setId(1L);
        mockUserDetailsDTO.setAdditionalInfo("Some additional information about the user");
    }

    @Test
    @WithMockUser
    void testGetAllUserDetails() throws Exception {
        when(userDetailsService.getAllUserDetails()).thenReturn(Collections.singletonList(mockUserDetailsDTO));

        mockMvc.perform(get("/user-details"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(mockUserDetailsDTO.getId()))
                .andExpect(jsonPath("$[0].additionalInfo").value(mockUserDetailsDTO.getAdditionalInfo()));

        verify(userDetailsService, times(1)).getAllUserDetails();
    }

    @Test
    @WithMockUser
    void testGetUserDetailsById() throws Exception {
        when(userDetailsService.getUserDetailsById(1L)).thenReturn(Optional.ofNullable(mockUserDetailsDTO));

        mockMvc.perform(get("/user-details/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(mockUserDetailsDTO.getId()))
                .andExpect(jsonPath("$.additionalInfo").value(mockUserDetailsDTO.getAdditionalInfo()));

        verify(userDetailsService, times(1)).getUserDetailsById(1L);
    }

    @Test
    @WithMockUser
    void testCreateUserDetails() throws Exception {
        when(userDetailsService.createUserDetails(any(UserDetailsDTO.class))).thenReturn(mockUserDetailsDTO);

        mockMvc.perform(post("/user-details")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockUserDetailsDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(mockUserDetailsDTO.getId()))
                .andExpect(jsonPath("$.additionalInfo").value(mockUserDetailsDTO.getAdditionalInfo()));

        verify(userDetailsService, times(1)).createUserDetails(any(UserDetailsDTO.class));
    }

    @Test
    @WithMockUser
    void testUpdateUserDetails() throws Exception {
        when(userDetailsService.updateUserDetails(eq(1L), any(UserDetailsDTO.class))).thenReturn(mockUserDetailsDTO);

        mockMvc.perform(put("/user-details/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockUserDetailsDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(mockUserDetailsDTO.getId()))
                .andExpect(jsonPath("$.additionalInfo").value(mockUserDetailsDTO.getAdditionalInfo()));

        verify(userDetailsService, times(1)).updateUserDetails(eq(1L), any(UserDetailsDTO.class));
    }

    @Test
    @WithMockUser
    void testDeleteUserDetails() throws Exception {
        doNothing().when(userDetailsService).deleteUserDetails(1L);

        mockMvc.perform(delete("/user-details/1"))
                .andExpect(status().isNoContent());

        verify(userDetailsService, times(1)).deleteUserDetails(1L);
    }
}
