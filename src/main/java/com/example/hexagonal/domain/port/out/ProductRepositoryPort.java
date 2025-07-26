package com.example.hexagonal.domain.port.out;

import java.util.List;
import java.util.Optional;

import com.example.hexagonal.domain.model.Product;

public interface ProductRepositoryPort {

    Product save(Product product);

    Optional<Product> findById(Long id);

    List<Product> findAll();

    Optional<Product> update(Long id, Product product);

    boolean deleteById(Long id);
}
