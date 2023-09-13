package com.app.onlinebookstore.dto.book;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Set;
import org.hibernate.validator.constraints.ISBN;
import org.hibernate.validator.constraints.URL;

public record CreateBookRequestDto(@NotBlank @Size(min = 1, max = 255) String title,
                                   @NotBlank @Size(min = 1, max = 255) String author,
                                   @NotBlank @ISBN String isbn,
                                   @NotBlank @Min(0) BigDecimal price,
                                   @NotBlank String description,
                                   @URL String coverImage,
                                   @NotNull @Size(min = 1) Set<Long> categories) {
}
