package com.example.lab1.service;

import com.example.lab1.domain.Product;
import com.example.lab1.dto.CreateProductRequest;
import com.example.lab1.dto.PagedResponse;
import com.example.lab1.dto.ProductResponseDto;
import com.example.lab1.exception.ProductNotFoundException;
import com.example.lab1.mapper.ProductMapper;
import com.example.lab1.repository.ProductMockRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductMockRepository repository;
    private final ProductMapper mapper;

    public ProductService(ProductMockRepository repository, ProductMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public PagedResponse<ProductResponseDto> getPagedProducts(int page, int size) {
        List<Product> all = repository.findAll();
        int totalElements = all.size();

        int fromIndex = Math.min(page * size, totalElements);
        int toIndex = Math.min(fromIndex + size, totalElements);

        List<ProductResponseDto> content = all.subList(fromIndex, toIndex).stream()
                .map(mapper::toDto)
                .toList();

        return new PagedResponse<>(content, page, size, totalElements);
    }

    public ProductResponseDto getById(String id) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        return mapper.toDto(product);
    }

    public ProductResponseDto create(CreateProductRequest request) {
        Product product = mapper.toDomain(request);
        Product saved = repository.save(product);
        return mapper.toDto(saved);
    }

    public void delete(String id) {
        if (!repository.deleteById(id)) {
            throw new ProductNotFoundException(id);
        }
    }
}