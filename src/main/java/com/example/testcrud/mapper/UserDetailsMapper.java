package com.example.testcrud.mapper;

import com.example.testcrud.dto.UserDetailsDTO;
import com.example.testcrud.model.UserProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserDetailsMapper {

    UserDetailsMapper INSTANCE = Mappers.getMapper(UserDetailsMapper.class);

    @Mapping(target = "additionalInfo", source = "userDetails.additionalInfo")
    UserDetailsDTO toDto(UserProfile userDetails);

    @Mapping(target = "additionalInfo", source = "userDetailsDTO.additionalInfo")
    UserProfile toEntity(UserDetailsDTO userDetailsDTO);

    default void updateEntity(UserDetailsDTO userDetailsDTO, UserProfile userDetails) {
        userDetails.setAdditionalInfo(userDetailsDTO.getAdditionalInfo());
    }
}
