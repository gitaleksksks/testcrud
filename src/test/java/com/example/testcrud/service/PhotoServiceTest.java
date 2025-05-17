package com.example.testcrud.service;

import com.example.testcrud.dto.PhotoDTO;
import com.example.testcrud.model.Photo;
import com.example.testcrud.repository.PhotoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class PhotoServiceTest {

    @Mock
    private PhotoRepository photoRepository;

    @InjectMocks
    private PhotoService photoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUploadPhoto() throws Exception {

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.jpg",
                "image/jpeg",
                "<<jpg data>>".getBytes()
        );

        when(photoRepository.save(any(Photo.class))).thenAnswer(invocation -> {
            Photo savedPhoto = invocation.getArgument(0);
            savedPhoto.setId(1L);
            return savedPhoto;
        });

        PhotoDTO result = photoService.uploadPhoto(file);

        assertNotNull(result);
        assertEquals("test.jpg", result.getFileName());
        assertEquals("image/jpeg", result.getFileType());

        verify(photoRepository, times(1)).save(any(Photo.class));
    }

    @Test
    void testGetPhotoByFileName_Success() {

        Photo mockPhoto = new Photo();
        mockPhoto.setId(1L);
        mockPhoto.setFileName("test.jpg");
        mockPhoto.setFileType("image/jpeg");
        mockPhoto.setData("<<jpg data>>".getBytes());

        when(photoRepository.findByFileName("test.jpg")).thenReturn(Optional.of(mockPhoto));

        PhotoDTO result = photoService.getPhotoByFileName("test.jpg").orElse(null);

        assertNotNull(result);
        assertEquals("test.jpg", result.getFileName());
        assertEquals("image/jpeg", result.getFileType());

        verify(photoRepository, times(1)).findByFileName("test.jpg");
    }

    @Test
    void testGetPhotoByFileName_NotFound() {

        when(photoRepository.findByFileName("nonexistent.jpg")).thenReturn(Optional.empty());

        Optional<PhotoDTO> result = photoService.getPhotoByFileName("nonexistent.jpg");

        assertTrue(result.isEmpty());

        verify(photoRepository, times(1)).findByFileName("nonexistent.jpg");
    }


    @Test
    void testDeletePhoto() {

        photoService.deletePhoto(1L);

        verify(photoRepository, times(1)).deleteById(1L);
    }
}
