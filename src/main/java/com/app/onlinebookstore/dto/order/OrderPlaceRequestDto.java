package com.app.onlinebookstore.dto.order;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record OrderPlaceRequestDto(
        @NotBlank @Length(max = 255) String shippingAddress
) {
}
