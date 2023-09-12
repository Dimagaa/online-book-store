package com.app.onlinebookstore.service;

import com.app.onlinebookstore.dto.user.UserDto;
import com.app.onlinebookstore.dto.user.UserRegisterRequestDto;
import com.app.onlinebookstore.exception.RegistrationException;

public interface UserService {
    UserDto register(UserRegisterRequestDto userRegisterRequestDto) throws RegistrationException;
}
