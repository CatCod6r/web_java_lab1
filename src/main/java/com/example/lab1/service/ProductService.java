package com.example.lab1.service;

import com.example.lab1.domain.Product;
import com.example.lab1.dto.CreateProductRequest;
import com.example.lab1.dto.ProductResponseDto;
import com.example.lab1.mapper.ProductMapper;
import com.example.lab1.repository.ProductMockRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductMockRepository repository;
    private final ProductMapper mapper;

    public ProductService(ProductMockRepository repository, ProductMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<ProductResponseDto> getAllProducts() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public ProductResponseDto getById(String id) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        return mapper.toDto(product);
    }

    public ProductResponseDto create(CreateProductRequest request) {
        Product product = mapper.toDomain(request);
        Product saved = repository.save(product);
        return mapper.toDto(saved);
    }

    public void delete(String id) {
        repository.deleteById(id);
    }
}
