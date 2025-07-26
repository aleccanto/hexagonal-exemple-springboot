package com.example.hexagonal.domain.port.in;

import java.util.List;
import java.util.Optional;

import com.example.hexagonal.domain.model.Order;

public interface OrderUseCase {

    Order createOrder(Order order);

    Optional<Order> getOrderById(Long id);

    List<Order> getAllOrders();

    Optional<Order> updateOrder(Long id, Order order);

    boolean deleteOrder(Long id);
}
