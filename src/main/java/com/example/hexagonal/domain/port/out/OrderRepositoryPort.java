package com.example.hexagonal.domain.port.out;

import java.util.List;
import java.util.Optional;

import com.example.hexagonal.domain.model.Order;

public interface OrderRepositoryPort {

    Order save(Order order);

    Optional<Order> findById(Long id);

    List<Order> findAll();

    Optional<Order> update(Long id, Order order);

    boolean deleteById(Long id);
}
