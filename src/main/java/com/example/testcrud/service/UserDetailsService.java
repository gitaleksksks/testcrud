package com.example.testcrud.service;

import com.example.testcrud.dto.UserDetailsDTO;
import com.example.testcrud.exception.ResourceNotFoundException;
import com.example.testcrud.mapper.UserDetailsMapper;
import com.example.testcrud.model.UserProfile;
import com.example.testcrud.repository.UserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserDetailsService {

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Autowired
    private UserDetailsMapper userDetailsMapper;

    public List<UserDetailsDTO> getAllUserDetails() {
        return userDetailsRepository.findAll().stream()
                .map(userDetailsMapper::toDto)
                .toList();
    }

    public Optional<UserDetailsDTO> getUserDetailsById(Long id) {
        return userDetailsRepository.findById(id)
                .map(userDetailsMapper::toDto);
    }

    public UserDetailsDTO createUserDetails(UserDetailsDTO userDetailsDTO) {
        UserProfile userDetails = userDetailsMapper.toEntity(userDetailsDTO);
        UserProfile savedDetails = userDetailsRepository.save(userDetails);
        return userDetailsMapper.toDto(savedDetails);
    }

    public UserDetailsDTO updateUserDetails(Long id, UserDetailsDTO userDetailsDTO) {
        UserProfile existingDetails = userDetailsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("UserDetails not found with id: " + id));
        userDetailsMapper.updateEntity(userDetailsDTO, existingDetails);
        UserProfile updatedDetails = userDetailsRepository.save(existingDetails);
        return userDetailsMapper.toDto(updatedDetails);
    }

    public void deleteUserDetails(Long id) {
        userDetailsRepository.deleteById(id);
    }
}
