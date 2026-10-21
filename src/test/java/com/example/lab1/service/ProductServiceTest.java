package com.example.lab1.service;

import com.example.lab1.domain.Product;
import com.example.lab1.dto.CreateProductRequest;
import com.example.lab1.dto.PagedResponse;
import com.example.lab1.dto.ProductResponseDto;
import com.example.lab1.exception.ProductNotFoundException;
import com.example.lab1.mapper.ProductMapper;
import com.example.lab1.repository.ProductMockRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProductService Unit Tests")
class ProductServiceTest {

    @Mock
    private ProductMockRepository repository;

    @Mock
    private ProductMapper mapper;

    @InjectMocks
    private ProductService productService;

    @Test
    @DisplayName("getPagedProducts returns paged items within boundary")
    void getPagedProducts_returnsPagedItems_whenValidPageAndSize() {
        Product product1 = new Product("1", "Star Candy", "Delicious", new BigDecimal("10.00"), "Food");
        Product product2 = new Product("2", "Galaxy Yarn", "Fun toy", new BigDecimal("15.00"), "Toys");
        Product product3 = new Product("3", "Moon Post", "Scratcher", new BigDecimal("20.00"), "Furniture");

        ProductResponseDto dto1 = new ProductResponseDto("1", "Star Candy", "Delicious", new BigDecimal("10.00"), "Food");
        ProductResponseDto dto2 = new ProductResponseDto("2", "Galaxy Yarn", "Fun toy", new BigDecimal("15.00"), "Toys");

        when(repository.findAll()).thenReturn(List.of(product1, product2, product3));
        when(mapper.toDto(product1)).thenReturn(dto1);
        when(mapper.toDto(product2)).thenReturn(dto2);

        PagedResponse<ProductResponseDto> response = productService.getPagedProducts(0, 2);

        assertNotNull(response);
        assertEquals(0, response.getPage());
        assertEquals(2, response.getSize());
        assertEquals(3, response.getTotalElements());
        assertEquals(2, response.getContent().size());
        assertEquals(dto1, response.getContent().get(0));
        assertEquals(dto2, response.getContent().get(1));

        verify(repository, times(1)).findAll();
        verify(mapper, times(1)).toDto(product1);
        verify(mapper, times(1)).toDto(product2);
    }

    @Test
    @DisplayName("getPagedProducts returns last partial page")
    void getPagedProducts_returnsLastPartialPage() {
        Product product1 = new Product("1", "Star Candy", "Delicious", new BigDecimal("10.00"), "Food");
        Product product2 = new Product("2", "Galaxy Yarn", "Fun toy", new BigDecimal("15.00"), "Toys");
        Product product3 = new Product("3", "Moon Post", "Scratcher", new BigDecimal("20.00"), "Furniture");

        ProductResponseDto dto3 = new ProductResponseDto("3", "Moon Post", "Scratcher", new BigDecimal("20.00"), "Furniture");

        when(repository.findAll()).thenReturn(List.of(product1, product2, product3));
        when(mapper.toDto(product3)).thenReturn(dto3);

        PagedResponse<ProductResponseDto> response = productService.getPagedProducts(1, 2);

        assertNotNull(response);
        assertEquals(1, response.getPage());
        assertEquals(2, response.getSize());
        assertEquals(3, response.getTotalElements());
        assertEquals(1, response.getContent().size());
        assertEquals(dto3, response.getContent().get(0));

        verify(repository, times(1)).findAll();
        verify(mapper, times(1)).toDto(product3);
    }

    @Test
    @DisplayName("getPagedProducts returns empty content when page is out of range")
    void getPagedProducts_returnsEmptyContent_whenPageExceedsElements() {
        Product product1 = new Product("1", "Star Candy", "Delicious", new BigDecimal("10.00"), "Food");
        when(repository.findAll()).thenReturn(List.of(product1));

        PagedResponse<ProductResponseDto> response = productService.getPagedProducts(5, 2);

        assertNotNull(response);
        assertEquals(5, response.getPage());
        assertEquals(2, response.getSize());
        assertEquals(1, response.getTotalElements());
        assertTrue(response.getContent().isEmpty());

        verify(repository, times(1)).findAll();
        verify(mapper, never()).toDto(any());
    }

    @Test
    @DisplayName("getPagedProducts returns empty content when repository is empty")
    void getPagedProducts_returnsEmptyContent_whenRepositoryIsEmpty() {
        when(repository.findAll()).thenReturn(Collections.emptyList());

        PagedResponse<ProductResponseDto> response = productService.getPagedProducts(0, 10);

        assertNotNull(response);
        assertEquals(0, response.getPage());
        assertEquals(10, response.getSize());
        assertEquals(0, response.getTotalElements());
        assertTrue(response.getContent().isEmpty());

        verify(repository, times(1)).findAll();
        verify(mapper, never()).toDto(any());
    }

    @Test
    @DisplayName("getById returns ProductResponseDto when product exists")
    void getById_returnsProduct_whenProductExists() {
        String id = "product-123";
        Product product = new Product(id, "Star Collar", "Cosmic collar", new BigDecimal("25.00"), "Apparel");
        ProductResponseDto expectedDto = new ProductResponseDto(id, "Star Collar", "Cosmic collar", new BigDecimal("25.00"), "Apparel");

        when(repository.findById(id)).thenReturn(Optional.of(product));
        when(mapper.toDto(product)).thenReturn(expectedDto);

        ProductResponseDto actualDto = productService.getById(id);

        assertNotNull(actualDto);
        assertEquals(expectedDto, actualDto);
        verify(repository, times(1)).findById(id);
        verify(mapper, times(1)).toDto(product);
    }

    @Test
    @DisplayName("getById throws ProductNotFoundException when product does not exist")
    void getById_throwsProductNotFoundException_whenProductDoesNotExist() {
        String id = "non-existent-id";
        when(repository.findById(id)).thenReturn(Optional.empty());

        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> productService.getById(id));

        assertTrue(exception.getMessage().contains(id));
        verify(repository, times(1)).findById(id);
        verify(mapper, never()).toDto(any());
    }

    @Test
    @DisplayName("create successfully maps, saves, and returns product")
    void create_savesAndReturnsProduct() {
        CreateProductRequest request = new CreateProductRequest();
        request.setName("Galaxy Ball");
        request.setDescription("Glows in the dark");
        request.setPrice(new BigDecimal("12.99"));
        request.setCategory("Toys");

        Product mappedProduct = new Product("generated-id", "Galaxy Ball", "Glows in the dark", new BigDecimal("12.99"), "Toys");
        Product savedProduct = new Product("generated-id", "Galaxy Ball", "Glows in the dark", new BigDecimal("12.99"), "Toys");
        ProductResponseDto expectedDto = new ProductResponseDto("generated-id", "Galaxy Ball", "Glows in the dark", new BigDecimal("12.99"), "Toys");

        when(mapper.toDomain(request)).thenReturn(mappedProduct);
        when(repository.save(mappedProduct)).thenReturn(savedProduct);
        when(mapper.toDto(savedProduct)).thenReturn(expectedDto);

        ProductResponseDto actualDto = productService.create(request);

        assertNotNull(actualDto);
        assertEquals(expectedDto, actualDto);
        verify(mapper, times(1)).toDomain(request);
        verify(repository, times(1)).save(mappedProduct);
        verify(mapper, times(1)).toDto(savedProduct);
    }

    @Test
    @DisplayName("delete successfully removes product when ID exists")
    void delete_removesProduct_whenIdExists() {
        String id = "product-123";
        when(repository.deleteById(id)).thenReturn(true);

        productService.delete(id);

        verify(repository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("delete throws ProductNotFoundException when ID does not exist")
    void delete_throwsProductNotFoundException_whenIdDoesNotExist() {
        String id = "missing-id";
        when(repository.deleteById(id)).thenReturn(false);

        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> productService.delete(id));

        assertTrue(exception.getMessage().contains(id));
        verify(repository, times(1)).deleteById(id);
    }
}
