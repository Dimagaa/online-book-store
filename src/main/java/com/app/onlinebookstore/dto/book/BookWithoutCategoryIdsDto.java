package com.app.onlinebookstore.dto.book;

import java.math.BigDecimal;

public record BookWithoutCategoryIdsDto(Long id,
                                        String title,
                                        String author,
                                        String isbn,
                                        BigDecimal price,
                                        String description,
                                        String coverImage) {
}
