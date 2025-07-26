package com.example.hexagonal.adapter.out.persistence.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.example.hexagonal.adapter.out.persistence.entity.ProductEntity;
import com.example.hexagonal.domain.model.Product;

public class ProductEntityMapper {

    public static Product toDomain(ProductEntity entity) {
        return new Product(entity.getId(), entity.getName(), entity.getDescription(), entity.getPrice());
    }

    public static ProductEntity fromDomain(Product product) {
        return new ProductEntity(product.getId(), product.getName(), product.getDescription(), product.getPrice());
    }

    public static List<Product> toDomain(List<ProductEntity> entities) {
        return entities.stream()
                .map(ProductEntityMapper::toDomain)
                .collect(Collectors.toList());
    }
}
