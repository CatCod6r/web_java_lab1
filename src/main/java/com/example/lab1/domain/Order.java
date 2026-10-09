package com.example.lab1.domain;

import java.math.BigDecimal;
import java.util.List;

public class Order {
    private String id;
    private String customerId;
    private List<Product> items;
    private BigDecimal totalAmount;

    public Order(String id, String customerId, List<Product> items, BigDecimal totalAmount) {
        this.id = id;
        this.customerId = customerId;
        this.items = items;
        this.totalAmount = totalAmount;
    }

    public String getId() { return id; }
    public String getCustomerId() { return customerId; }
    public List<Product> getItems() { return items; }
    public BigDecimal getTotalAmount() { return totalAmount; }
}