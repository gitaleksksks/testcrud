package com.example.testcrud.controller;

import com.example.testcrud.dto.UserDetailsDTO;
import com.example.testcrud.exception.ResourceNotFoundException;
import com.example.testcrud.service.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user-details")
public class UserDetailsController {

    @Autowired
    private UserDetailsService userDetailsService;

    @GetMapping
    public ResponseEntity<List<UserDetailsDTO>> getAllUserDetails() {
        List<UserDetailsDTO> userDetailsList = userDetailsService.getAllUserDetails();
        return ResponseEntity.ok(userDetailsList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDetailsDTO> getUserDetailsById(@PathVariable Long id) {
        UserDetailsDTO userDetails = userDetailsService.getUserDetailsById(id)
                .orElseThrow(() -> new ResourceNotFoundException("UserDetails not found with id: " + id));
        return ResponseEntity.ok(userDetails);
    }

    @PostMapping
    public ResponseEntity<UserDetailsDTO> createUserDetails(@RequestBody UserDetailsDTO userDetailsDTO) {
        UserDetailsDTO createdDetails = userDetailsService.createUserDetails(userDetailsDTO);
        return ResponseEntity.status(201).body(createdDetails);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDetailsDTO> updateUserDetails(@PathVariable Long id, @RequestBody UserDetailsDTO userDetailsDTO) {
        UserDetailsDTO updatedDetails = userDetailsService.updateUserDetails(id, userDetailsDTO);
        return ResponseEntity.ok(updatedDetails);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserDetails(@PathVariable Long id) {
        userDetailsService.deleteUserDetails(id);
        return ResponseEntity.noContent().build();
    }
}
