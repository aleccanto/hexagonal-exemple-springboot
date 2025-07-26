package com.example.hexagonal.adapter.in.web;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.hexagonal.adapter.in.web.dto.ProductDTO;
import com.example.hexagonal.domain.model.Product;
import com.example.hexagonal.domain.port.in.ProductUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(ProductController.class)
class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductUseCase productUseCase;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createProduct() throws Exception {
        ProductDTO productDTO = new ProductDTO(null, "Test Product", "Description", 10.0);
        Product product = new Product(1L, "Test Product", "Description", 10.0);

        when(productUseCase.createProduct(any(Product.class))).thenReturn(product);

        mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test Product"));
    }

    @Test
    void getProductById() throws Exception {
        Product product = new Product(1L, "Test Product", "Description", 10.0);
        when(productUseCase.getProductById(1L)).thenReturn(Optional.of(product));

        mockMvc.perform(get("/products/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test Product"));
    }

    @Test
    void getAllProducts() throws Exception {
        when(productUseCase.getAllProducts()).thenReturn(Arrays.asList(
                new Product(1L, "Product 1", "Desc 1", 10.0),
                new Product(2L, "Product 2", "Desc 2", 20.0)
        ));

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Product 1"))
                .andExpect(jsonPath("$[1].name").value("Product 2"));
    }

    @Test
    void updateProduct() throws Exception {
        ProductDTO productDTO = new ProductDTO(null, "Updated Product", "Updated Description", 15.0);
        Product updatedProduct = new Product(1L, "Updated Product", "Updated Description", 15.0);

        when(productUseCase.updateProduct(eq(1L), any(Product.class))).thenReturn(Optional.of(updatedProduct));

        mockMvc.perform(put("/products/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Product"));
    }

    @Test
    void deleteProduct() throws Exception {
        when(productUseCase.deleteProduct(1L)).thenReturn(true);

        mockMvc.perform(delete("/products/{id}", 1L))
                .andExpect(status().isNoContent());
    }
}
