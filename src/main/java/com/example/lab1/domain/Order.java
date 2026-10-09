package com.example.lab1.domain;

import java.util.List;

public class Order {
    private String id;
    private String customerId;
    private List<Product> items;
    private Double totalAmount;

    public Double getTotalAmount() { return totalAmount; }
}
