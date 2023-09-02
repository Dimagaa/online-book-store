package com.app.onlinebookstore.repository.book.specification;

import com.app.onlinebookstore.model.Book;
import com.app.onlinebookstore.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class TitleSpecificationProvider implements SpecificationProvider<Book> {
    private static final String KEY_FIELD = "title";
    private static final String SEARCH_FORMAT = "%%%s%%";
    private static final int PARAMETER_INDEX = 0;

    @Override
    public Specification<Book> getSpecification(String[] params) {
        String searchExpression = SEARCH_FORMAT.formatted(params[PARAMETER_INDEX]);
        return (root, query, criteriaBuilder) -> query.where(
                        criteriaBuilder.like(root.get(KEY_FIELD), searchExpression))
                .getRestriction();
    }

    @Override
    public String getKey() {
        return KEY_FIELD;
    }
}
