package com.example.hexagonal.adapter.in.web;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
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

import com.example.hexagonal.adapter.in.web.dto.OrderDTO;
import com.example.hexagonal.adapter.in.web.dto.OrderItemDTO;
import com.example.hexagonal.domain.model.Order;
import com.example.hexagonal.domain.model.OrderItem;
import com.example.hexagonal.domain.port.in.OrderUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@WebMvcTest(OrderController.class)
class OrderControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderUseCase orderUseCase;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void createOrder() throws Exception {
        OrderItemDTO itemDTO1 = new OrderItemDTO(null, 1L, "Product A", 2, 10.0);
        OrderItemDTO itemDTO2 = new OrderItemDTO(null, 2L, "Product B", 1, 25.0);
        OrderDTO orderDTO = new OrderDTO(null, "Customer 1", LocalDateTime.now(), Arrays.asList(itemDTO1, itemDTO2), 0.0);

        OrderItem item1 = new OrderItem(null, 1L, "Product A", 2, 10.0);
        OrderItem item2 = new OrderItem(null, 2L, "Product B", 1, 25.0);
        Order order = new Order(1L, "Customer 1", LocalDateTime.now(), Arrays.asList(item1, item2), 45.0);

        when(orderUseCase.createOrder(any(Order.class))).thenReturn(order);

        mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.totalAmount").value(45.0));
    }

    @Test
    void getOrderById() throws Exception {
        Order order = new Order(1L, "Customer 1", LocalDateTime.now(), Collections.emptyList(), 100.0);
        when(orderUseCase.getOrderById(1L)).thenReturn(Optional.of(order));

        mockMvc.perform(get("/orders/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.customerName").value("Customer 1"));
    }

    @Test
    void getAllOrders() throws Exception {
        when(orderUseCase.getAllOrders()).thenReturn(Arrays.asList(
                new Order(1L, "Customer 1", LocalDateTime.now(), Collections.emptyList(), 100.0),
                new Order(2L, "Customer 2", LocalDateTime.now(), Collections.emptyList(), 200.0)
        ));

        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].customerName").value("Customer 1"))
                .andExpect(jsonPath("$[1].customerName").value("Customer 2"));
    }

    @Test
    void updateOrder() throws Exception {
        OrderItemDTO itemDTO1 = new OrderItemDTO(null, 1L, "Product A", 2, 10.0);
        OrderDTO orderDTO = new OrderDTO(null, "Updated Customer", LocalDateTime.now(), Arrays.asList(itemDTO1), 150.0);

        OrderItem item1 = new OrderItem(null, 1L, "Product A", 2, 10.0);
        Order updatedOrder = new Order(1L, "Updated Customer", LocalDateTime.now(), Arrays.asList(item1), 150.0);

        when(orderUseCase.updateOrder(eq(1L), any(Order.class))).thenReturn(Optional.of(updatedOrder));

        mockMvc.perform(put("/orders/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerName").value("Updated Customer"));
    }

    @Test
    void deleteOrder() throws Exception {
        when(orderUseCase.deleteOrder(1L)).thenReturn(true);

        mockMvc.perform(delete("/orders/{id}", 1L))
                .andExpect(status().isNoContent());
    }
}
