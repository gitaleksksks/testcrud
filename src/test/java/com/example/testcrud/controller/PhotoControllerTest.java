package com.example.testcrud.controller;

import com.example.testcrud.dto.PhotoDTO;
import com.example.testcrud.filter.JwtRequestFilter;
import com.example.testcrud.repository.UserRepository;
import com.example.testcrud.security.JwtUtil;
import com.example.testcrud.security.MyUserDetailsService;
import com.example.testcrud.service.PhotoService;
import com.example.testcrud.config.SecurityConfig;
import com.example.testcrud.service.UserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PhotoController.class)
@Import({SecurityConfig.class, MyUserDetailsService.class, JwtRequestFilter.class, JwtUtil.class})
public class PhotoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PhotoService photoService;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private PhotoDTO mockPhotoDTO;

    @BeforeEach
    void setUp() {
        mockPhotoDTO = new PhotoDTO();
        mockPhotoDTO.setId(1L);
        mockPhotoDTO.setFileName("test.jpg");
        mockPhotoDTO.setFileType("image/jpeg");
    }

    @Test
    @WithMockUser
    void testGetAllPhotos() throws Exception {
        when(photoService.getAllPhotos()).thenReturn(Collections.singletonList(mockPhotoDTO));

        mockMvc.perform(MockMvcRequestBuilders.get("/photos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(mockPhotoDTO.getId()))
                .andExpect(jsonPath("$[0].fileName").value(mockPhotoDTO.getFileName()))
                .andExpect(jsonPath("$[0].fileType").value(mockPhotoDTO.getFileType()));

        verify(photoService, times(1)).getAllPhotos();
    }

    @Test
    @WithMockUser
    void testUploadPhoto() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.jpg",
                "image/jpeg",
                "<<jpg data>>".getBytes()
        );

        when(photoService.uploadPhoto(any())).thenReturn(mockPhotoDTO);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/photos")
                        .file(file))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(mockPhotoDTO.getId()))
                .andExpect(jsonPath("$.fileName").value(mockPhotoDTO.getFileName()))
                .andExpect(jsonPath("$.fileType").value(mockPhotoDTO.getFileType()));

        verify(photoService, times(1)).uploadPhoto(any());
    }

    @Test
    @WithMockUser
    void testDeletePhoto() throws Exception {
        doNothing().when(photoService).deletePhoto(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/photos/1"))
                .andExpect(status().isNoContent());

        verify(photoService, times(1)).deletePhoto(1L);
    }
}
