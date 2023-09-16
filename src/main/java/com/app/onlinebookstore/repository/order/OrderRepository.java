package com.app.onlinebookstore.repository.order;

import com.app.onlinebookstore.model.Order;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("""
            FROM Order o
            LEFT JOIN FETCH o.user
            LEFT JOIN FETCH o.orderItems i
            LEFT JOIN FETCH i.book
            """)
    List<Order> findAllWithUserAndOrderItems(Pageable pageable);
}
