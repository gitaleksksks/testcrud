package com.example.testcrud.mapper;

import com.example.testcrud.dto.UserDTO;
import com.example.testcrud.dto.UserDetailsDTO;
import com.example.testcrud.dto.PhotoDTO;
import com.example.testcrud.model.User;
import com.example.testcrud.model.UserProfile;
import com.example.testcrud.model.Photo;

public class UserMapper {

    public static UserDTO toDto(User user) {
        if (user == null) {
            return null;
        }

        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setLastName(user.getLastName());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setMiddleName(user.getMiddleName());
        userDTO.setBirthDate(user.getBirthDate());
        userDTO.setEmail(user.getEmail());
        userDTO.setPhone(user.getPhone());

        if (user.getUserProfile() != null) {
            UserDetailsDTO userDetailsDTO = new UserDetailsDTO();
            userDetailsDTO.setId(user.getUserProfile().getId());
            userDetailsDTO.setAdditionalInfo(user.getUserProfile().getAdditionalInfo());
            userDTO.setUserDetails(userDetailsDTO);
        }

        if (user.getPhoto() != null) {
            PhotoDTO photoDTO = new PhotoDTO();
            photoDTO.setId(user.getPhoto().getId());
            photoDTO.setFileName(user.getPhoto().getFileName());
            photoDTO.setFileType(user.getPhoto().getFileType());
            userDTO.setPhoto(photoDTO);
        }

        return userDTO;
    }

    public static User toEntity(UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        }

        User user = new User();
        user.setId(userDTO.getId());
        user.setLastName(userDTO.getLastName());
        user.setFirstName(userDTO.getFirstName());
        user.setMiddleName(userDTO.getMiddleName());
        user.setBirthDate(userDTO.getBirthDate());
        user.setEmail(userDTO.getEmail());
        user.setPhone(userDTO.getPhone());

        if (userDTO.getUserDetails() != null) {
            UserProfile userProfile = new UserProfile();
            userProfile.setId(userDTO.getUserDetails().getId());
            userProfile.setAdditionalInfo(userDTO.getUserDetails().getAdditionalInfo());
            user.setUserProfile(userProfile);
        }

        if (userDTO.getPhoto() != null) {
            Photo photo = new Photo();
            photo.setId(userDTO.getPhoto().getId());
            photo.setFileName(userDTO.getPhoto().getFileName());
            photo.setFileType(userDTO.getPhoto().getFileType());
            user.setPhoto(photo);
        }

        return user;
    }

    public static void updateEntity(UserDTO userDTO, User user) {
        if (userDTO == null || user == null) {
            return;
        }

        user.setLastName(userDTO.getLastName());
        user.setFirstName(userDTO.getFirstName());
        user.setMiddleName(userDTO.getMiddleName());
        user.setBirthDate(userDTO.getBirthDate());
        user.setEmail(userDTO.getEmail());
        user.setPhone(userDTO.getPhone());

        if (userDTO.getUserDetails() != null) {
            if (user.getUserProfile() == null) {
                user.setUserProfile(new UserProfile());
            }
            user.getUserProfile().setAdditionalInfo(userDTO.getUserDetails().getAdditionalInfo());
        }

        if (userDTO.getPhoto() != null) {
            if (user.getPhoto() == null) {
                user.setPhoto(new Photo());
            }
            user.getPhoto().setFileName(userDTO.getPhoto().getFileName());
            user.getPhoto().setFileType(userDTO.getPhoto().getFileType());
        }
    }
}
