package com.app.onlinebookstore.service.impl;

import com.app.onlinebookstore.dto.user.UserDto;
import com.app.onlinebookstore.dto.user.UserRegisterRequestDto;
import com.app.onlinebookstore.exception.RegistrationException;
import com.app.onlinebookstore.mapper.UserMapper;
import com.app.onlinebookstore.model.Role;
import com.app.onlinebookstore.model.User;
import com.app.onlinebookstore.repository.user.RoleRepository;
import com.app.onlinebookstore.repository.user.UserRepository;
import com.app.onlinebookstore.service.UserService;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public UserDto register(UserRegisterRequestDto userRegisterRequestDto)
            throws RegistrationException {
        if (userRepository.findByEmail(userRegisterRequestDto.email()).isPresent()) {
            throw new RegistrationException("Unable to complete registration");
        }
        User user = userMapper.toModel(userRegisterRequestDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role role = roleRepository.getRoleByName(Role.RoleName.ROLE_USER);
        user.setRoles(Set.of(role));
        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    @Override
    public User getAuthenticatedUser() {
        return (User) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
    }
}
