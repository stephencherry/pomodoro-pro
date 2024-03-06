package com.platform.pomodoropro.entity.model;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ModelMapper {
    ModelMapper INSTANCE = Mappers.getMapper(ModelMapper.class);

    @Mapping(target = "id", expression = "java(entity.getId())")
    @Mapping(target = "username", expression = "java(entity.getUsername())")
    @Mapping(target = "firstname", expression = "java(entity.getFirstname())")
    @Mapping(target = "lastname", expression = "java(entity.getLastname())")
    @Mapping(target = "email", expression = "java(entity.getEmail())")
    @Mapping(target = "phone", expression = "java(entity.getPhone().toString())")
    @Mapping(target = "status", expression = "java(entity.getStatus().toString())")
    @Mapping(target = "role", expression = "java(entity.getRole().toString())")
    @Mapping(target = "gender", expression = "java(entity.getGender().toString())")
    UserMapper mapUserEntityToUserMapper(UserEntity userEntity);
}