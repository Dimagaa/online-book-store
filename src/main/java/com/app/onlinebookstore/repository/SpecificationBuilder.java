package com.app.onlinebookstore.repository;

import org.springframework.data.jpa.domain.Specification;

public interface SpecificationBuilder<T, U> {
    Specification<T> build(U searchParameters);
}
