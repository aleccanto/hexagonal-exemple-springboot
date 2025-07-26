package com.example.hexagonal.adapter.out.persistence;

import com.example.hexagonal.adapter.out.persistence.entity.OrderEntity;
import com.example.hexagonal.adapter.out.persistence.mapper.OrderEntityMapper;
import com.example.hexagonal.adapter.out.persistence.repository.OrderJpaRepository;
import com.example.hexagonal.domain.model.Order;
import com.example.hexagonal.domain.port.out.OrderRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class OrderRepositoryAdapter implements OrderRepositoryPort {

    private final OrderJpaRepository orderJpaRepository;

    public OrderRepositoryAdapter(OrderJpaRepository orderJpaRepository) {
        this.orderJpaRepository = orderJpaRepository;
    }

    @Override
    public Order save(Order order) {
        OrderEntity orderEntity = OrderEntityMapper.fromDomain(order);
        OrderEntity savedOrderEntity = orderJpaRepository.save(orderEntity);
        return OrderEntityMapper.toDomain(savedOrderEntity);
    }

    @Override
    public Optional<Order> findById(Long id) {
        return orderJpaRepository.findById(id).map(OrderEntityMapper::toDomain);
    }

    @Override
    public List<Order> findAll() {
        return OrderEntityMapper.toDomain(orderJpaRepository.findAll());
    }

    @Override
    public Optional<Order> update(Long id, Order order) {
        if (orderJpaRepository.existsById(id)) {
            OrderEntity orderEntity = OrderEntityMapper.fromDomain(order);
            orderEntity.setId(id);
            OrderEntity updatedOrderEntity = orderJpaRepository.save(orderEntity);
            return Optional.of(OrderEntityMapper.toDomain(updatedOrderEntity));
        }
        return Optional.empty();
    }

    @Override
    public boolean deleteById(Long id) {
        if (orderJpaRepository.existsById(id)) {
            orderJpaRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
