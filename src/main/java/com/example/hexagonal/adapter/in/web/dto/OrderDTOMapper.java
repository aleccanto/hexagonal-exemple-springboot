package com.example.hexagonal.adapter.in.web.dto;

import com.example.hexagonal.domain.model.Order;
import com.example.hexagonal.domain.model.OrderItem;

import java.util.List;
import java.util.stream.Collectors;

public class OrderDTOMapper {

    public static Order toDomain(OrderDTO dto) {
        List<OrderItem> items = dto.getItems().stream()
                .map(OrderItemDTOMapper::toDomain)
                .collect(Collectors.toList());
        return new Order(dto.getId(), dto.getCustomerName(), dto.getOrderDate(), items, dto.getTotalAmount());
    }

    public static OrderDTO fromDomain(Order order) {
        List<OrderItemDTO> items = order.getItems().stream()
                .map(OrderItemDTOMapper::fromDomain)
                .collect(Collectors.toList());
        return new OrderDTO(order.getId(), order.getCustomerName(), order.getOrderDate(), items, order.getTotalAmount());
    }

    public static List<OrderDTO> fromDomain(List<Order> orders) {
        return orders.stream()
                .map(OrderDTOMapper::fromDomain)
                .collect(Collectors.toList());
    }
}
