package com.example.hexagonal.adapter.in.web.dto;

import com.example.hexagonal.domain.model.OrderItem;

public class OrderItemDTOMapper {

    public static OrderItem toDomain(OrderItemDTO dto) {
        return new OrderItem(dto.getId(), dto.getProductId(), dto.getProductName(), dto.getQuantity(), dto.getPrice());
    }

    public static OrderItemDTO fromDomain(OrderItem orderItem) {
        return new OrderItemDTO(orderItem.getId(), orderItem.getProductId(), orderItem.getProductName(), orderItem.getQuantity(), orderItem.getPrice());
    }
}
