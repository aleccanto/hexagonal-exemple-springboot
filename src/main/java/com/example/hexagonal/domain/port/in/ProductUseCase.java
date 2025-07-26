package com.example.hexagonal.domain.port.in;

import java.util.List;
import java.util.Optional;

import com.example.hexagonal.domain.model.Product;

public interface ProductUseCase {

    Product createProduct(Product product);

    Optional<Product> getProductById(Long id);

    List<Product> getAllProducts();

    Optional<Product> updateProduct(Long id, Product product);

    boolean deleteProduct(Long id);
}
