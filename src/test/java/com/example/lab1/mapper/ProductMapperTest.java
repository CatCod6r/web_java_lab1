package com.example.lab1.mapper;

import com.example.lab1.domain.Product;
import com.example.lab1.dto.CreateProductRequest;
import com.example.lab1.dto.ProductResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@DisplayName("ProductMapper Unit Tests")
class ProductMapperTest {

    private final ProductMapper mapper = new ProductMapper();

    @Test
    @DisplayName("toDomain maps CreateProductRequest correctly")
    void toDomain_mapsRequestSuccessfully() {
        CreateProductRequest request = new CreateProductRequest();
        request.setName("Nebula Ball");
        request.setDescription("Purple bouncy ball");
        request.setPrice(new BigDecimal("9.99"));
        request.setCategory("Toys");

        Product product = mapper.toDomain(request);

        assertNotNull(product);
        assertNotNull(product.getId());
        assertEquals("Nebula Ball", product.getName());
        assertEquals("Purple bouncy ball", product.getDescription());
        assertEquals(new BigDecimal("9.99"), product.getPrice());
        assertEquals("Toys", product.getCategory());
    }

    @Test
    @DisplayName("toDomain returns null when request is null")
    void toDomain_returnsNull_whenNullInput() {
        assertNull(mapper.toDomain(null));
    }

    @Test
    @DisplayName("toDto maps Product to ProductResponseDto correctly")
    void toDto_mapsProductSuccessfully() {
        Product product = new Product("prod-1", "Solar Scratch Post", "Tall scratcher", new BigDecimal("49.99"), "Furniture");

        ProductResponseDto dto = mapper.toDto(product);

        assertNotNull(dto);
        assertEquals("prod-1", dto.getId());
        assertEquals("Solar Scratch Post", dto.getName());
        assertEquals("Tall scratcher", dto.getDescription());
        assertEquals(new BigDecimal("49.99"), dto.getPrice());
        assertEquals("Furniture", dto.getCategory());
    }

    @Test
    @DisplayName("toDto returns null when product is null")
    void toDto_returnsNull_whenNullInput() {
        assertNull(mapper.toDto(null));
    }
}
