package com.app.onlinebookstore.repository.shoppingcart;

import com.app.onlinebookstore.model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
}
