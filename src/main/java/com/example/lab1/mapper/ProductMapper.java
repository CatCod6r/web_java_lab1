package com.example.lab1.mapper;

import com.example.lab1.domain.Product;
import com.example.lab1.dto.CreateProductRequest;
import com.example.lab1.dto.ProductResponseDto;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ProductMapper {

    public Product toDomain(CreateProductRequest request) {
        if (request == null) return null;
        return new Product(
                UUID.randomUUID().toString(),
                request.getName(),
                request.getDescription(),
                request.getPrice(),
                request.getCategory()
        );
    }

    public ProductResponseDto toDto(Product product) {
        if (product == null) return null;
        return new ProductResponseDto(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getCategory()
        );
    }
}
