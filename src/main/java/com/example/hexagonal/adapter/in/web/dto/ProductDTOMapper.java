package com.example.hexagonal.adapter.in.web.dto;

import com.example.hexagonal.domain.model.Product;

import java.util.List;
import java.util.stream.Collectors;

public class ProductDTOMapper {

    public static Product toDomain(ProductDTO dto) {
        return new Product(dto.getId(), dto.getName(), dto.getDescription(), dto.getPrice());
    }

    public static ProductDTO fromDomain(Product product) {
        return new ProductDTO(product.getId(), product.getName(), product.getDescription(), product.getPrice());
    }

    public static List<ProductDTO> fromDomain(List<Product> products) {
        return products.stream()
                .map(ProductDTOMapper::fromDomain)
                .collect(Collectors.toList());
    }
}
