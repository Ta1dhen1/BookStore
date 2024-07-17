package com.example.BookStore.repositories;

import com.example.BookStore.providers.Order;
import com.example.BookStore.providers.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByPersonOrderByOrderDateDesc(Person person);
    List<Order> findAllByOrderByOrderDateDesc();
}
