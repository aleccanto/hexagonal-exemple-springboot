package com.example.hexagonal.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.hexagonal.domain.port.in.OrderUseCase;
import com.example.hexagonal.domain.port.in.ProductUseCase;
import com.example.hexagonal.domain.port.out.OrderRepositoryPort;
import com.example.hexagonal.domain.port.out.ProductRepositoryPort;
import com.example.hexagonal.domain.service.OrderService;
import com.example.hexagonal.domain.service.ProductService;

@Configuration
public class AppConfig {

    @Bean
    public ProductUseCase productUseCase(ProductRepositoryPort productRepositoryPort) {
        return new ProductService(productRepositoryPort);
    }

    @Bean
    public OrderUseCase orderUseCase(OrderRepositoryPort orderRepositoryPort) {
        return new OrderService(orderRepositoryPort);
    }
}
