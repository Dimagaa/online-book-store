package com.app.onlinebookstore.dto.category;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record CategoryRequestDto(
        @NotBlank String name,
        @Length(min = 1, max = 255) String description) {
}
