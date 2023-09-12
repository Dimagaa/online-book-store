package com.app.onlinebookstore.dto.user;

import com.app.onlinebookstore.validation.ElementsEqual;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

@ElementsEqual(firstFieldName = "password",
        secondFieldName = "repeatPassword",
        message = "Passwords don't match")
public record UserRegisterRequestDto(@NotBlank
                              @Email
                              String email,
                                     @NotBlank
                              @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{6,}$",
                                      message = "Minimum eight characters, at least one uppercase"
                                              + " letter, one lowercase letter and one number")
                              String password,
                                     @NotBlank
                              String repeatPassword,
                                     @NotBlank
                              @Length(min = 1, max = 255)
                              String firstName,
                                     @NotBlank
                              @Length(min = 1, max = 255)
                              String lastName,
                                     @Length(min = 1, max = 255)
                              String shippingAddress) {
}
