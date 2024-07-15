package com.example.BookStore.repositories;

import com.example.BookStore.providers.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}
