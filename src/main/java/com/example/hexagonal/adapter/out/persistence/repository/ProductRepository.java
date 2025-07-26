package com.example.hexagonal.adapter.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.hexagonal.adapter.out.persistence.entity.ProductEntity;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
}
