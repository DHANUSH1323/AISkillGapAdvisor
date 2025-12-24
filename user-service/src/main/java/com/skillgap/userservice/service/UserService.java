package com.skillgap.userservice.service;

import com.skillgap.userservice.model.dto.response.UserResponseDto;

public interface UserService {
    UserResponseDto getOrCreateCurrentUser();
}
