package com.example.lab1.domain;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private String id;
    private String userId;
    private List<Product> products = new ArrayList<>();

    public Cart(String id, String userId) {
        this.id = id;
        this.userId = userId;
    }

    public String getId() { return id; }
    public String getUserId() { return userId; }
    public List<Product> getProducts() { return products; }
}
