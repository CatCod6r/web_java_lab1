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
}
