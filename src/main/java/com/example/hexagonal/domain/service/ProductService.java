package com.example.hexagonal.domain.service;

import com.example.hexagonal.domain.model.Product;
import com.example.hexagonal.domain.port.in.ProductUseCase;
import com.example.hexagonal.domain.port.out.ProductRepositoryPort;

import java.util.List;
import java.util.Optional;

public class ProductService implements ProductUseCase {

    private final ProductRepositoryPort productRepositoryPort;

    public ProductService(ProductRepositoryPort productRepositoryPort) {
        this.productRepositoryPort = productRepositoryPort;
    }

    @Override
    public Product createProduct(Product product) {
        return productRepositoryPort.save(product);
    }

    @Override
    public Optional<Product> getProductById(Long id) {
        return productRepositoryPort.findById(id);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepositoryPort.findAll();
    }

    @Override
    public Optional<Product> updateProduct(Long id, Product product) {
        return productRepositoryPort.update(id, product);
    }

    @Override
    public boolean deleteProduct(Long id) {
        return productRepositoryPort.deleteById(id);
    }
}
