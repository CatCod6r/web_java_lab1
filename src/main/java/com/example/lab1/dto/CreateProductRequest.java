package com.example.lab1.dto;

import jakarta.validation.constraints.NotNull;

public class CreateProductRequest {
    @NotNull
    private String name;

    private String description;

    @NotNull
    private Double price;

    private String category;

    public CreateProductRequest() {}
}
