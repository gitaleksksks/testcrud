package com.example.testcrud.service;

import com.example.testcrud.dto.PhotoDTO;
import com.example.testcrud.exception.ResourceNotFoundException;
import com.example.testcrud.mapper.PhotoMapper;
import com.example.testcrud.model.Photo;
import com.example.testcrud.repository.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class PhotoService {

    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private PhotoMapper photoMapper;

    public List<PhotoDTO> getAllPhotos() {
        return photoRepository.findAll().stream()
                .map(photoMapper::toDto)
                .toList();
    }

    public Optional<PhotoDTO> getPhotoById(Long id) {
        return photoRepository.findById(id)
                .map(photoMapper::toDto);
    }

    public Optional<PhotoDTO> getPhotoByFileName(String fileName) {
        return photoRepository.findByFileName(fileName)
                .map(photoMapper::toDto);
    }

    public PhotoDTO uploadPhoto(MultipartFile file) {
        try {
            Photo photo = new Photo();
            photo.setFileName(file.getOriginalFilename());
            photo.setFileType(file.getContentType());
            photo.setData(file.getBytes());
            Photo savedPhoto = photoRepository.save(photo);
            return photoMapper.toDto(savedPhoto);
        } catch (IOException e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }

    public PhotoDTO updatePhoto(Long id, MultipartFile file) {
        Photo existingPhoto = photoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Photo not found with id: " + id));

        try {
            existingPhoto.setFileName(file.getOriginalFilename());
            existingPhoto.setFileType(file.getContentType());
            existingPhoto.setData(file.getBytes());
            Photo updatedPhoto = photoRepository.save(existingPhoto);
            return photoMapper.toDto(updatedPhoto);
        } catch (IOException e) {
            throw new RuntimeException("Could not update the file. Error: " + e.getMessage());
        }
    }

    public void deletePhoto(Long id) {
        photoRepository.deleteById(id);
    }
}
