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
        database.add(new Product("1", "Anti-gravity Yarn Star", "Space toy for kittens", 19.99, "Toys"));
        database.add(new Product("2", "Cosmic Milk Galaxy", "Nutritious drink", 5.50, "Food"));
    }

    public List<Product> findAll() {
        return new ArrayList<>(database);
    }

    public Optional<Product> findById(String id) {
        return database.stream().filter(p -> p.getId().equals(id)).findFirst();
    }

    public Product save(Product product) {
        database.removeIf(p -> p.getId().equals(product.getId()));
        database.add(product);
        return product;
    }

    public boolean deleteById(String id) {
        return database.removeIf(p -> p.getId().equals(id));
    }
}
