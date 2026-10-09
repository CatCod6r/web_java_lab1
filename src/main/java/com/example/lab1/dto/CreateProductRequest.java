package com.example.lab1.dto;

import jakarta.validation.constraints.NotNull;

public class CreateProductRequest {
    // SMELL 2: Відсутня кастомна перевірка @CosmicWordCheck та @NotBlank
    @NotNull
    private String name;

    private String description;

    // SMELL 3: Double замість BigDecimal, немає валідації @Positive
    @NotNull
    private Double price;

    private String category;

    public CreateProductRequest() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
}
