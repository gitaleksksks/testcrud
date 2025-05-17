package com.example.testcrud.controller;

import com.example.testcrud.dto.PhotoDTO;
import com.example.testcrud.exception.ResourceNotFoundException;
import com.example.testcrud.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/photos")
public class PhotoController {

    @Autowired
    private PhotoService photoService;

    @GetMapping
    public ResponseEntity<List<PhotoDTO>> getAllPhotos() {
        List<PhotoDTO> photos = photoService.getAllPhotos();
        return ResponseEntity.ok(photos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PhotoDTO> getPhotoById(@PathVariable Long id) {
        PhotoDTO photo = photoService.getPhotoById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Photo not found with id: " + id));
        return ResponseEntity.ok(photo);
    }

    @PostMapping
    public ResponseEntity<PhotoDTO> uploadPhoto(@RequestParam("file") MultipartFile file) {
        PhotoDTO uploadedPhoto = photoService.uploadPhoto(file);
        return ResponseEntity.status(201).body(uploadedPhoto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PhotoDTO> updatePhoto(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        PhotoDTO updatedPhoto = photoService.updatePhoto(id, file);
        return ResponseEntity.ok(updatedPhoto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePhoto(@PathVariable Long id) {
        photoService.deletePhoto(id);
        return ResponseEntity.noContent().build();
    }
}
