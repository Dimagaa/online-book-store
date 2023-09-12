package com.app.onlinebookstore.service;

import com.app.onlinebookstore.dto.user.RegisterUserDto;
import com.app.onlinebookstore.dto.user.UserDto;
import com.app.onlinebookstore.exception.RegistrationException;

public interface UserService {
    UserDto register(RegisterUserDto registerUserDto) throws RegistrationException;
}
