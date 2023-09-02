package com.app.onlinebookstore.repository.book;

import com.app.onlinebookstore.dto.BookSearchParameters;
import com.app.onlinebookstore.model.Book;
import com.app.onlinebookstore.repository.SpecificationBuilder;
import com.app.onlinebookstore.repository.SpecificationProviderManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationBuilder implements SpecificationBuilder<Book, BookSearchParameters> {
    private static final String AUTHOR_KEY = "author";
    private static final String TITLE_KEY = "title";
    private static final String ISBN_KEY = "isbn";
    private final SpecificationProviderManager<Book> bookSpecificationProviderManager;

    @Override
    public Specification<Book> build(BookSearchParameters searchParameters) {
        Specification<Book> specification = Specification.where(null);
        if (searchParameters.author() != null && searchParameters.author().length > 0) {
            specification = specification.and(bookSpecificationProviderManager
                    .getSpecificationParameter(AUTHOR_KEY)
                    .getSpecification(searchParameters.author()));
        }
        if (searchParameters.isbn() != null && searchParameters.isbn().length > 0) {
            specification = specification.and(bookSpecificationProviderManager
                    .getSpecificationParameter(ISBN_KEY)
                    .getSpecification(searchParameters.isbn()));
        }
        if (searchParameters.title() != null && !searchParameters.title().isEmpty()) {
            specification = specification.and(bookSpecificationProviderManager
                    .getSpecificationParameter(TITLE_KEY)
                    .getSpecification(new String[]{searchParameters.title()}));
        }
        return specification;
    }

}
