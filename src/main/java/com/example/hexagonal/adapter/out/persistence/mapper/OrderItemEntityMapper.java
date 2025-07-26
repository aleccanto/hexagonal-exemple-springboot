package com.example.hexagonal.adapter.out.persistence.mapper;

import com.example.hexagonal.adapter.out.persistence.entity.OrderItemEntity;
import com.example.hexagonal.domain.model.OrderItem;

public class OrderItemEntityMapper {

    public static OrderItem toDomain(OrderItemEntity entity) {
        return new OrderItem(entity.getId(), entity.getProductId(), entity.getProductName(), entity.getQuantity(), entity.getPrice());
    }

    public static OrderItemEntity fromDomain(OrderItem orderItem) {
        return new OrderItemEntity(orderItem.getId(), orderItem.getProductId(), orderItem.getProductName(), orderItem.getQuantity(), orderItem.getPrice());
    }
}
