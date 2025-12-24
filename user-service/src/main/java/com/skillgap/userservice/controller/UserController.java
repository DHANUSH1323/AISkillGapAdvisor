package com.skillgap.userservice.controller;

import com.skillgap.userservice.model.dto.response.UserResponseDto;
import com.skillgap.userservice.service.UserService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users/me")
    public UserResponseDto getCurrentUser() {
        return userService.getOrCreateCurrentUser();
    }
}