package com.app.onlinebookstore.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import org.hibernate.validator.constraints.URL;

public record CreateBookRequestDto(@NotNull @NotBlank String title,
                                   @NotNull @NotBlank String author,
                                   @NotNull @Size(min = 13, max = 13) String isbn,
                                   @NotNull @Positive BigDecimal price,
                                   @NotNull @NotBlank String description,
                                   @NotNull @URL String coverImage) {
}
