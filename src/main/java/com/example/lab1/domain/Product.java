package com.example.lab1.domain;

public class Product {
    private String id;
    private String name;
    private String description;
    // SMELL 1: Double замість BigDecimal
    private Double price;
    private String category;

    public Product() {}

    public void setCategory(String category) { this.category = category; }
}
