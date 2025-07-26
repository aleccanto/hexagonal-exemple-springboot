package com.example.hexagonal;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.hexagonal.adapter.in.web.dto.OrderDTO;
import com.example.hexagonal.adapter.in.web.dto.OrderItemDTO;
import com.example.hexagonal.adapter.in.web.dto.ProductDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@SpringBootTest
@AutoConfigureMockMvc
class FullIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void productAndOrderCrudFlow() throws Exception {
        // --- Product CRUD Flow ---

        // 1. Create Product 1
        ProductDTO product1 = new ProductDTO(null, "Laptop", "High performance laptop", 1200.00);
        MvcResult result1 = mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andReturn();
        Long productId1 = objectMapper.readTree(result1.getResponse().getContentAsString()).get("id").asLong();

        // 2. Create Product 2
        ProductDTO product2 = new ProductDTO(null, "Mouse", "Wireless mouse", 25.00);
        MvcResult result2 = mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product2)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andReturn();
        Long productId2 = objectMapper.readTree(result2.getResponse().getContentAsString()).get("id").asLong();

        // 3. Get Product 1 by ID
        mockMvc.perform(get("/products/{id}", productId1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(productId1))
                .andExpect(jsonPath("$.name").value("Laptop"));

        // 4. Update Product 1
        ProductDTO updatedProduct1 = new ProductDTO(productId1, "Gaming Laptop", "High performance gaming laptop", 1500.00);
        mockMvc.perform(put("/products/{id}", productId1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedProduct1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Gaming Laptop"))
                .andExpect(jsonPath("$.price").value(1500.00));

        // 5. Get All Products
        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Gaming Laptop"))
                .andExpect(jsonPath("$[1].name").value("Mouse"));

        // --- Order CRUD Flow ---
        // 1. Create Order
        OrderItemDTO orderItem1 = new OrderItemDTO(null, productId1, "Gaming Laptop", 1, 1500.00);
        OrderItemDTO orderItem2 = new OrderItemDTO(null, productId2, "Wireless Mouse", 2, 25.00);
        List<OrderItemDTO> orderItems = Arrays.asList(orderItem1, orderItem2);

        OrderDTO newOrder = new OrderDTO(null, "John Doe", LocalDateTime.now(), orderItems, 0.0);
        MvcResult orderResult = mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newOrder)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.customerName").value("John Doe"))
                .andExpect(jsonPath("$.totalAmount").value(1550.00)) // 1*1500 + 2*25 = 1550
                .andReturn();
        Long orderId = objectMapper.readTree(orderResult.getResponse().getContentAsString()).get("id").asLong();

        // 2. Get Order by ID and verify
        mockMvc.perform(get("/orders/{id}", orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(orderId))
                .andExpect(jsonPath("$.customerName").value("John Doe"))
                .andExpect(jsonPath("$.totalAmount").value(1550.00))
                .andExpect(jsonPath("$.items[0].productId").value(productId1))
                .andExpect(jsonPath("$.items[0].quantity").value(1))
                .andExpect(jsonPath("$.items[1].productId").value(productId2))
                .andExpect(jsonPath("$.items[1].quantity").value(2));

        // 3. Delete Order
        mockMvc.perform(delete("/orders/{id}", orderId))
                .andExpect(status().isNoContent());

        // 4. Try to Get Deleted Order (should be Not Found)
        mockMvc.perform(get("/orders/{id}", orderId))
                .andExpect(status().isNotFound());

        // --- Cleanup Products ---
        mockMvc.perform(delete("/products/{id}", productId1))
                .andExpect(status().isNoContent());
        mockMvc.perform(delete("/products/{id}", productId2))
                .andExpect(status().isNoContent());
    }
}
