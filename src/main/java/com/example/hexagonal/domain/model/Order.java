package com.example.hexagonal.domain.model;

import java.time.LocalDateTime;
import java.util.List;

public class Order {

    private Long id;
    private String customerName;
    private LocalDateTime orderDate;
    private List<OrderItem> items;
    private double totalAmount;

    public Order() {
    }

    public Order(Long id, String customerName, LocalDateTime orderDate, List<OrderItem> items, double totalAmount) {
        this.id = id;
        this.customerName = customerName;
        this.orderDate = orderDate;
        this.items = items;
        this.totalAmount = totalAmount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
}
