package com.example.hexagonal.adapter.out.persistence.mapper;

import com.example.hexagonal.adapter.out.persistence.entity.OrderEntity;
import com.example.hexagonal.adapter.out.persistence.entity.OrderItemEntity;
import com.example.hexagonal.domain.model.Order;
import com.example.hexagonal.domain.model.OrderItem;

import java.util.List;
import java.util.stream.Collectors;

public class OrderEntityMapper {

    public static Order toDomain(OrderEntity entity) {
        List<OrderItem> items = entity.getItems().stream()
                .map(OrderItemEntityMapper::toDomain)
                .collect(Collectors.toList());
        return new Order(entity.getId(), entity.getCustomerName(), entity.getOrderDate(), items, entity.getTotalAmount());
    }

    public static OrderEntity fromDomain(Order order) {
        List<OrderItemEntity> items = order.getItems().stream()
                .map(OrderItemEntityMapper::fromDomain)
                .collect(Collectors.toList());
        return new OrderEntity(order.getId(), order.getCustomerName(), order.getOrderDate(), items, order.getTotalAmount());
    }

    public static List<Order> toDomain(List<OrderEntity> entities) {
        return entities.stream()
                .map(OrderEntityMapper::toDomain)
                .collect(Collectors.toList());
    }
}
