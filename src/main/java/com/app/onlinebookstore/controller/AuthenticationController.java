package com.app.onlinebookstore.controller;

import com.app.onlinebookstore.dto.user.UserDto;
import com.app.onlinebookstore.dto.user.UserLoginRequestDto;
import com.app.onlinebookstore.dto.user.UserLoginResponseDto;
import com.app.onlinebookstore.dto.user.UserRegisterRequestDto;
import com.app.onlinebookstore.exception.RegistrationException;
import com.app.onlinebookstore.security.AuthenticationService;
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
public class AuthenticationController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public UserDto register(@RequestBody @Valid UserRegisterRequestDto request)
            throws RegistrationException {
        return userService.register(request);
    }

    @PostMapping("/login")
    public UserLoginResponseDto login(@RequestBody @Valid UserLoginRequestDto request) {
        return authenticationService.authenticate(request);
    }
}
