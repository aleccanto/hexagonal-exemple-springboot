package com.example.hexagonal.domain.service;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.example.hexagonal.domain.model.Product;
import com.example.hexagonal.domain.port.out.ProductRepositoryPort;

class ProductServiceTest {

    @Mock
    private ProductRepositoryPort productRepositoryPort;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createProduct() {
        Product product = new Product(null, "Test Product", "Description", 10.0);
        when(productRepositoryPort.save(any(Product.class))).thenReturn(new Product(1L, "Test Product", "Description", 10.0));

        Product createdProduct = productService.createProduct(product);

        assertNotNull(createdProduct.getId());
        assertEquals("Test Product", createdProduct.getName());
        verify(productRepositoryPort, times(1)).save(any(Product.class));
    }

    @Test
    void getProductById() {
        Product product = new Product(1L, "Test Product", "Description", 10.0);
        when(productRepositoryPort.findById(1L)).thenReturn(Optional.of(product));

        Optional<Product> foundProduct = productService.getProductById(1L);

        assertTrue(foundProduct.isPresent());
        assertEquals("Test Product", foundProduct.get().getName());
        verify(productRepositoryPort, times(1)).findById(1L);
    }

    @Test
    void getAllProducts() {
        when(productRepositoryPort.findAll()).thenReturn(Arrays.asList(
                new Product(1L, "Product 1", "Desc 1", 10.0),
                new Product(2L, "Product 2", "Desc 2", 20.0)
        ));

        assertEquals(2, productService.getAllProducts().size());
        verify(productRepositoryPort, times(1)).findAll();
    }

    @Test
    void updateProduct() {
        Product existingProduct = new Product(1L, "Old Name", "Old Desc", 10.0);
        Product updatedProduct = new Product(1L, "New Name", "New Desc", 15.0);

        when(productRepositoryPort.update(eq(1L), any(Product.class))).thenReturn(Optional.of(updatedProduct));

        Optional<Product> result = productService.updateProduct(1L, updatedProduct);

        assertTrue(result.isPresent());
        assertEquals("New Name", result.get().getName());
        verify(productRepositoryPort, times(1)).update(eq(1L), any(Product.class));
    }

    @Test
    void deleteProduct() {
        when(productRepositoryPort.deleteById(1L)).thenReturn(true);

        assertTrue(productService.deleteProduct(1L));
        verify(productRepositoryPort, times(1)).deleteById(1L);
    }
}
