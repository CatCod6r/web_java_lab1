package com.example.lab1.repository;

import com.example.lab1.domain.Product;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
public class ProductMockRepository {
    private final List<Product> database = new CopyOnWriteArrayList<>();

    public ProductMockRepository() {
}
