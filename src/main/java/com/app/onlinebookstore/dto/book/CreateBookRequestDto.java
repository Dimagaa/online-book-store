package com.app.onlinebookstore.dto.book;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import org.hibernate.validator.constraints.ISBN;
import org.hibernate.validator.constraints.URL;

public record CreateBookRequestDto(@NotNull @Size(min = 1, max = 255) String title,
                                   @NotNull @Size(min = 1, max = 255) String author,
                                   @NotNull @ISBN String isbn,
                                   @NotNull @Min(0) BigDecimal price,
                                   @NotBlank String description,
                                   @URL String coverImage) {
}
