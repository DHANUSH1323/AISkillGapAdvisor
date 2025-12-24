package com.skillgap.userservice.model.mapper;

import com.skillgap.userservice.model.dto.response.UserResponseDto;
import com.skillgap.userservice.model.entity.UserEntity;

import java.util.Objects;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class UserMapper extends BaseMapper<UserEntity, UserResponseDto>{

    @Override
    public UserEntity convertToEntity(UserResponseDto dto, Object... args){
        UserEntity entity = new UserEntity();
        if(Objects.nonNull(dto)){
            BeanUtils.copyProperties(dto, entity);
        }
        return entity;
    }

    @Override
    public UserResponseDto convertToDto(UserEntity entity, Object... args){
        UserResponseDto dto = new UserResponseDto();
        if(Objects.nonNull(entity)){
            BeanUtils.copyProperties(entity, dto);
        }
        return dto;
    }
}
