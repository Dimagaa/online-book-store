package com.app.onlinebookstore.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record LoginUserDto(@NotNull
                           @Email
                           String email,
                           @NotNull
                           @Length(min = 6)
                           String password) {
}
