package com.app.onlinebookstore.controller;

import com.app.onlinebookstore.dto.user.RegisterUserDto;
import com.app.onlinebookstore.dto.user.UserDto;
import com.app.onlinebookstore.exception.RegistrationException;
import com.app.onlinebookstore.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;

    @PostMapping("/register")
    public UserDto register(@RequestBody @Valid RegisterUserDto registerUserDto)
            throws RegistrationException {
        return userService.register(registerUserDto);
    }
}
