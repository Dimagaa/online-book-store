package com.app.onlinebookstore.repository.book.specification;

import com.app.onlinebookstore.model.Book;
import com.app.onlinebookstore.repository.SpecificationProvider;
import java.util.Arrays;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class IsbnSpecificationProvider implements SpecificationProvider<Book> {
    private static final String KEY_FIELD = "isbn";

    @Override
    public Specification<Book> getSpecification(String[] params) {
        return ((root, query, criteriaBuilder) -> root.get(KEY_FIELD)
                .in(Arrays.stream(params).toArray()));
    }

    @Override
    public String getKey() {
        return KEY_FIELD;
    }
}
