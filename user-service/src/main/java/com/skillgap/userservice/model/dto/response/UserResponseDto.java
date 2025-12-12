package com.skillgap.userservice.model.dto.response;

import java.util.UUID;
import com.skillgap.userservice.model.UserStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseDto {

    private UUID id;
    private String username;
    private String email;
    private UserStatus status;
}
