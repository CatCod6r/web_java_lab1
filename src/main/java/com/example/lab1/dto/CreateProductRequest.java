package com.example.lab1.dto;

import jakarta.validation.constraints.NotNull;

public class CreateProductRequest {
    // SMELL 2: Відсутня кастомна перевірка @CosmicWordCheck та @NotBlank
    @NotNull
    private String name;

    private String description;

    public void setCategory(String category) { this.category = category; }
}
