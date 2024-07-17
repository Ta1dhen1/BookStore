package com.example.BookStore.repositories;

import com.example.BookStore.providers.Cart;
import com.example.BookStore.providers.Person;
import jakarta.persistence.Transient;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.transaction.Transactional;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
    public Cart findByPersonIdAndBookId(Integer person_id, Integer book_id);
    public List<Cart> findByPersonId(Integer userId);
    public List<Cart> findAllByPersonRole(String personRole);
    public Integer countByPersonId(Integer person_id);
    public List<Cart> findByPerson(Person person);
    @Transactional
    void deleteAllByBookId(int id);
}
