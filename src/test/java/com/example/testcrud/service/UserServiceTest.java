package com.example.testcrud.service;

import com.example.testcrud.dto.UserDTO;
import com.example.testcrud.exception.BadRequestException;
import com.example.testcrud.exception.ResourceNotFoundException;
import com.example.testcrud.mapper.UserMapper;
import com.example.testcrud.model.User;
import com.example.testcrud.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllUsers() {

        List<User> mockUsers = new ArrayList<>();
        User user1 = new User();
        user1.setId(1L);
        user1.setFirstName("Ivan");
        user1.setLastName("Ivanov");
        user1.setEmail("ivan.ivanov@example.com");

        User user2 = new User();
        user2.setId(2L);
        user2.setFirstName("Petr");
        user2.setLastName("Petrov");
        user2.setEmail("petr.petrov@example.com");

        mockUsers.add(user1);
        mockUsers.add(user2);

        when(userRepository.findAll()).thenReturn(mockUsers);

        List<UserDTO> result = userService.getAllUsers();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Ivan", result.get(0).getFirstName());
        assertEquals("Petr", result.get(1).getFirstName());

        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testGetUserById_Success() {

        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setFirstName("Ivan");
        mockUser.setLastName("Ivanov");
        mockUser.setEmail("ivan.ivanov@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));

        Optional<UserDTO> result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals(1L, result.get().getId());
        assertEquals("Ivan", result.get().getFirstName());
        assertEquals("Ivanov", result.get().getLastName());

        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testGetUserById_NotFound() {

        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            userService.getUserById(2L);
        });

        assertEquals("User not found with id: 2", exception.getMessage());

        verify(userRepository, times(1)).findById(2L);
    }

    @Test
    void testCreateUser() {

        UserDTO mockUserDTO = new UserDTO();
        mockUserDTO.setFirstName("Ivan");
        mockUserDTO.setLastName("Ivanov");
        mockUserDTO.setEmail("ivan.ivanov@example.com");

        when(userRepository.existsByEmail("ivan.ivanov@example.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            savedUser.setId(1L);
            return savedUser;
        });

        UserDTO result = userService.createUser(mockUserDTO);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Ivan", result.getFirstName());
        assertEquals("Ivanov", result.getLastName());

        verify(userRepository, times(1)).existsByEmail("ivan.ivanov@example.com");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testCreateUser_EmailAlreadyExists() {

        UserDTO mockUserDTO = new UserDTO();
        mockUserDTO.setEmail("ivan.ivanov@example.com");

        when(userRepository.existsByEmail("ivan.ivanov@example.com")).thenReturn(true);

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            userService.createUser(mockUserDTO);
        });

        assertEquals("User with this email already exists", exception.getMessage());

        verify(userRepository, times(1)).existsByEmail("ivan.ivanov@example.com");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testUpdateUser() {

        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setFirstName("Ivan");
        existingUser.setLastName("Ivanov");
        existingUser.setEmail("ivan.ivanov@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserDTO updatedUserDTO = new UserDTO();
        updatedUserDTO.setId(1L);
        updatedUserDTO.setFirstName("UpdatedFirstName");
        updatedUserDTO.setLastName("UpdatedLastName");

        UserDTO result = userService.updateUser(1L, updatedUserDTO);

        assertNotNull(result);
        assertEquals("UpdatedFirstName", result.getFirstName());
        assertEquals("UpdatedLastName", result.getLastName());

        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(any(User.class));
    }


    @Test
    void testDeleteUser() {

        User mockUser = new User();
        mockUser.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));

        userService.deleteUser(1L);

        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).delete(mockUser);
    }
}
