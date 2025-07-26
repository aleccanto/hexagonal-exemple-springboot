package com.example.hexagonal.adapter.out.persistence.repository;

import com.example.hexagonal.adapter.out.persistence.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderJpaRepository extends JpaRepository<OrderEntity, Long> {
}
