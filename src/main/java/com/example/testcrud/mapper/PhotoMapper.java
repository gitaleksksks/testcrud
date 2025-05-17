package com.example.testcrud.mapper;

import com.example.testcrud.dto.PhotoDTO;
import com.example.testcrud.model.Photo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PhotoMapper {

    PhotoMapper INSTANCE = Mappers.getMapper(PhotoMapper.class);

    @Mapping(target = "fileName", source = "photo.fileName")
    @Mapping(target = "fileType", source = "photo.fileType")
    PhotoDTO toDto(Photo photo);

    @Mapping(target = "fileName", source = "photoDTO.fileName")
    @Mapping(target = "fileType", source = "photoDTO.fileType")
    Photo toEntity(PhotoDTO photoDTO);
}
