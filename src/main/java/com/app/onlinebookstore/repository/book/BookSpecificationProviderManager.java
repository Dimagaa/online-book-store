package com.app.onlinebookstore.repository.book;

import com.app.onlinebookstore.model.Book;
import com.app.onlinebookstore.repository.SpecificationProvider;
import com.app.onlinebookstore.repository.SpecificationProviderManager;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationProviderManager implements SpecificationProviderManager<Book> {
    private final List<SpecificationProvider<Book>> bookSpecificationProviders;

    @Override
    public SpecificationProvider<Book> getSpecificationParameter(String key) {
        return bookSpecificationProviders.stream()
                .filter(prov -> prov.getKey().equals(key))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(
                        "Can't find a specification provider for key " + key)
                );
    }
}
