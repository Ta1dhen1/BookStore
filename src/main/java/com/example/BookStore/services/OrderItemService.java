package com.example.BookStore.services;

import com.example.BookStore.repositories.OrderItemRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderItemService {
    private final OrderItemRepository orderItemRepository;

    public OrderItemService(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    public void deleteAll(int id){
        orderItemRepository.deleteAllByBookId(id);
    }
}

