package com.example.BookStore.repositories;

import com.example.BookStore.providers.Order;
import com.example.BookStore.providers.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    List<Order> findAllByPersonOrderByOrderDateDesc(Person person);
    List<Order> findAllByOrderByOrderDateDesc();
}
