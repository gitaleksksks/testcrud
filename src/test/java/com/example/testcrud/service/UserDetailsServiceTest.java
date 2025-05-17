package com.example.testcrud.service;

import com.example.testcrud.dto.UserDetailsDTO;
import com.example.testcrud.exception.BadRequestException;
import com.example.testcrud.mapper.UserDetailsMapper;
import com.example.testcrud.model.UserProfile;
import com.example.testcrud.repository.UserDetailsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class UserDetailsServiceTest {

    @Mock
    private UserDetailsRepository userDetailsRepository;

    @InjectMocks
    private UserDetailsService userDetailsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUserDetails_Success() {

        UserDetailsDTO mockUserDetailsDTO = new UserDetailsDTO();
        mockUserDetailsDTO.setAdditionalInfo("Some additional information");

        when(userDetailsRepository.save(any(UserProfile.class))).thenAnswer(invocation -> {
            UserProfile savedDetails = invocation.getArgument(0);
            savedDetails.setId(1L);
            return savedDetails;
        });

        UserDetailsDTO result = userDetailsService.createUserDetails(mockUserDetailsDTO);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Some additional information", result.getAdditionalInfo());

        verify(userDetailsRepository, times(1)).save(any(UserProfile.class));
    }


    @Test
    void testGetUserDetailsById_Success() {

        UserProfile mockUserDetails = new UserProfile();
        mockUserDetails.setId(1L);
        mockUserDetails.setAdditionalInfo("Some additional information");

        when(userDetailsRepository.findById(1L)).thenReturn(Optional.of(mockUserDetails));

        UserDetailsDTO result = userDetailsService.getUserDetailsById(1L).orElse(null);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Some additional information", result.getAdditionalInfo());

        verify(userDetailsRepository, times(1)).findById(1L);
    }


    @Test
    void testGetUserDetailsById_NotFound() {

        when(userDetailsRepository.findById(2L)).thenReturn(Optional.empty());

        Optional<UserDetailsDTO> result = userDetailsService.getUserDetailsById(2L);

        assertTrue(result.isEmpty());

        verify(userDetailsRepository, times(1)).findById(2L);
    }


    @Test
    void testUpdateUserDetails() {

        UserProfile existingDetails = new UserProfile();
        existingDetails.setId(1L);
        existingDetails.setAdditionalInfo("Old information");

        when(userDetailsRepository.findById(1L)).thenReturn(Optional.of(existingDetails));
        when(userDetailsRepository.save(any(UserProfile.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserDetailsDTO updatedDetailsDTO = new UserDetailsDTO();
        updatedDetailsDTO.setId(1L);
        updatedDetailsDTO.setAdditionalInfo("Updated information");

        UserDetailsDTO result = userDetailsService.updateUserDetails(1L, updatedDetailsDTO);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Updated information", result.getAdditionalInfo());

        verify(userDetailsRepository, times(1)).findById(1L);
        verify(userDetailsRepository, times(1)).save(any(UserProfile.class));
    }

    @Test
    void testDeleteUserDetails() {

        userDetailsService.deleteUserDetails(1L);

        verify(userDetailsRepository, times(1)).deleteById(1L);
    }
}
