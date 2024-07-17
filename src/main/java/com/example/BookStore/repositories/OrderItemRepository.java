package com.example.BookStore.repositories;

import com.example.BookStore.providers.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {

    void deleteAllByBookId(int id);
}
