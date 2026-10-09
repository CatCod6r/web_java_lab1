package com.example.lab1.dto;

import com.example.lab1.validation.CosmicWordCheck;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public class CreateProductRequest {

    @NotBlank(message = "Product name must not be blank")
    @CosmicWordCheck
    private String name;

    private String description;

    @NotNull(message = "Price is required")
    @Positive(message = "Field price must be greater than 0.")
    private BigDecimal price;

    @NotBlank(message = "Category must not be blank")
    private String category;

    public CreateProductRequest() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
}