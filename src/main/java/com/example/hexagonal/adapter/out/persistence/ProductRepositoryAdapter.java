package com.example.hexagonal.adapter.out.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.example.hexagonal.adapter.out.persistence.entity.ProductEntity;
import com.example.hexagonal.adapter.out.persistence.mapper.ProductEntityMapper;
import com.example.hexagonal.adapter.out.persistence.repository.ProductRepository;
import com.example.hexagonal.domain.model.Product;
import com.example.hexagonal.domain.port.out.ProductRepositoryPort;

@Component
public class ProductRepositoryAdapter implements ProductRepositoryPort {

    private final ProductRepository productRepository;

    public ProductRepositoryAdapter(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product save(Product product) {
        ProductEntity productEntity = ProductEntityMapper.fromDomain(product);
        ProductEntity savedProductEntity = productRepository.save(productEntity);
        return ProductEntityMapper.toDomain(savedProductEntity);
    }

    @Override
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id).map(ProductEntityMapper::toDomain);
    }

    @Override
    public List<Product> findAll() {
        return ProductEntityMapper.toDomain(productRepository.findAll());
    }

    @Override
    public Optional<Product> update(Long id, Product product) {
        if (productRepository.existsById(id)) {
            ProductEntity productEntity = ProductEntityMapper.fromDomain(product);
            productEntity.setId(id);
            ProductEntity updatedProductEntity = productRepository.save(productEntity);
            return Optional.of(ProductEntityMapper.toDomain(updatedProductEntity));
        }
        return Optional.empty();
    }

    @Override
    public boolean deleteById(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
