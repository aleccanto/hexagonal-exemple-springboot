package com.example.hexagonal.domain.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
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

import com.example.hexagonal.domain.model.Order;
import com.example.hexagonal.domain.model.OrderItem;
import com.example.hexagonal.domain.port.out.OrderRepositoryPort;

class OrderServiceTest {

    @Mock
    private OrderRepositoryPort orderRepositoryPort;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createOrder() {
        OrderItem item1 = new OrderItem(null, 1L, "Product A", 2, 10.0);
        OrderItem item2 = new OrderItem(null, 2L, "Product B", 1, 25.0);
        Order order = new Order(null, "Customer 1", LocalDateTime.now(), Arrays.asList(item1, item2), 0.0);

        when(orderRepositoryPort.save(any(Order.class))).thenAnswer(invocation -> {
            Order savedOrder = invocation.getArgument(0);
            savedOrder.setId(1L);
            return savedOrder;
        });

        Order createdOrder = orderService.createOrder(order);

        assertNotNull(createdOrder.getId());
        assertEquals(45.0, createdOrder.getTotalAmount()); // (2 * 10.0) + (1 * 25.0) = 45.0
        verify(orderRepositoryPort, times(1)).save(any(Order.class));
    }

    @Test
    void getOrderById() {
        Order order = new Order(1L, "Customer 1", LocalDateTime.now(), Collections.emptyList(), 100.0);
        when(orderRepositoryPort.findById(1L)).thenReturn(Optional.of(order));

        Optional<Order> foundOrder = orderService.getOrderById(1L);

        assertTrue(foundOrder.isPresent());
        assertEquals("Customer 1", foundOrder.get().getCustomerName());
        verify(orderRepositoryPort, times(1)).findById(1L);
    }

    @Test
    void getAllOrders() {
        when(orderRepositoryPort.findAll()).thenReturn(Arrays.asList(
                new Order(1L, "Customer 1", LocalDateTime.now(), Collections.emptyList(), 100.0),
                new Order(2L, "Customer 2", LocalDateTime.now(), Collections.emptyList(), 200.0)
        ));

        assertEquals(2, orderService.getAllOrders().size());
        verify(orderRepositoryPort, times(1)).findAll();
    }

    @Test
    void updateOrder() {
        Order existingOrder = new Order(1L, "Old Customer", LocalDateTime.now(), Collections.emptyList(), 100.0);
        Order updatedOrder = new Order(1L, "New Customer", LocalDateTime.now(), Collections.emptyList(), 150.0);

        when(orderRepositoryPort.update(eq(1L), any(Order.class))).thenReturn(Optional.of(updatedOrder));

        Optional<Order> result = orderService.updateOrder(1L, updatedOrder);

        assertTrue(result.isPresent());
        assertEquals("New Customer", result.get().getCustomerName());
        verify(orderRepositoryPort, times(1)).update(eq(1L), any(Order.class));
    }

    @Test
    void deleteOrder() {
        when(orderRepositoryPort.deleteById(1L)).thenReturn(true);

        assertTrue(orderService.deleteOrder(1L));
        verify(orderRepositoryPort, times(1)).deleteById(1L);
    }
}
