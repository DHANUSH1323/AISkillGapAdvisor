package com.skillgap.userservice.service.implementation;

import com.skillgap.userservice.model.UserStatus;
import com.skillgap.userservice.model.dto.response.UserResponseDto;
import com.skillgap.userservice.model.entity.UserEntity;
import com.skillgap.userservice.model.mapper.UserMapper;
import com.skillgap.userservice.repository.UserRepository;
import com.skillgap.userservice.service.UserService;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService{
    
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper){
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserResponseDto getOrCreateCurrentUser(){
        Jwt jwt = (Jwt) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        String keycloakUserId = jwt.getSubject();
        String username = jwt.getClaimAsString("preferred_username");
        String email = jwt.getClaimAsString("email");

        Optional<UserEntity> existingUser =
                userRepository.findBykeycloakUserId(keycloakUserId);

        if (existingUser.isPresent()) {
            return userMapper.convertToDto(existingUser.get());
        }

        UserEntity newUser = UserEntity.builder()
                .keycloakUserId(keycloakUserId)
                .username(username)
                .email(email)
                .status(UserStatus.ACTIVE)
                .build();

        UserEntity savedUser = userRepository.save(newUser);

        return userMapper.convertToDto(savedUser);

    }


    
}
