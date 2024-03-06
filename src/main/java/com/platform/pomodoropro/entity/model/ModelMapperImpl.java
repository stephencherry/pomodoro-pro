package com.platform.pomodoropro.entity.model;

import javax.annotation.processing.Generated;

@Generated(
        value = "org.mapstruct.ap.MappingProcessor",
        date = "2023-12-18T09:31:00+0100",
        comments = "version: 1.5.3.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.4.jar, environment: Java 17.0.9 (Debian)"
)
public class ModelMapperImpl implements ModelMapper{
    @Override
    public UserMapper mapUserEntityToUserMapper(UserEntity userEntity) {
        if(userEntity == null){
            return null;
        }
        UserMapper userMapper = new UserMapper();

        userMapper.setCreatedDate( userEntity.getCreatedDate() );
        userMapper.setUpdatedDate( userEntity.getUpdatedDate() );

        userMapper.setId(userEntity.getId());
        userMapper.username = userEntity.getUsername();
        userMapper.firstname = userEntity.getFirstname();
        userMapper.lastname = userEntity.getLastname();
        userMapper.email = userEntity.getEmail();
        userMapper.status = userEntity.getStatus().toString();
        userMapper.role = userEntity.getRole().toString();
        userMapper.gender = userEntity.getGender().toString();
        return userMapper;
    }
}
