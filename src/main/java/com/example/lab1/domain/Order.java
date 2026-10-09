package com.example.lab1.domain;

import java.util.List;

public class Order {
    private String id;
    private String customerId;
    private List<Product> items;
    private Double totalAmount;

    public Order(String id, String customerId, List<Product> items, Double totalAmount) {
        this.id = id;
        this.customerId = customerId;
        this.items = items;
        this.totalAmount = totalAmount;
    }

    public String getId() { return id; }
    public String getCustomerId() { return customerId; }
    public List<Product> getItems() { return items; }
    public Double getTotalAmount() { return totalAmount; }
}
