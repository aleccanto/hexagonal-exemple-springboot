package com.example.hexagonal.domain.service;

import java.util.List;
import java.util.Optional;

import com.example.hexagonal.domain.model.Order;
import com.example.hexagonal.domain.port.in.OrderUseCase;
import com.example.hexagonal.domain.port.out.OrderRepositoryPort;

public class OrderService implements OrderUseCase {

    private final OrderRepositoryPort orderRepositoryPort;

    public OrderService(OrderRepositoryPort orderRepositoryPort) {
        this.orderRepositoryPort = orderRepositoryPort;
    }

    @Override
    public Order createOrder(Order order) {
        order.setTotalAmount(order.getItems().stream().mapToDouble(item -> item.getQuantity() * item.getPrice()).sum());
        return orderRepositoryPort.save(order);
    }

    @Override
    public Optional<Order> getOrderById(Long id) {
        return orderRepositoryPort.findById(id);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepositoryPort.findAll();
    }

    @Override
    public Optional<Order> updateOrder(Long id, Order order) {
        return orderRepositoryPort.update(id, order);
    }

    @Override
    public boolean deleteOrder(Long id) {
        return orderRepositoryPort.deleteById(id);
    }
}
