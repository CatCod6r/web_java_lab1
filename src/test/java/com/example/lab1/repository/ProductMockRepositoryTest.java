package com.example.lab1.repository;

import com.example.lab1.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("ProductMockRepository Unit Tests")
class ProductMockRepositoryTest {

    private ProductMockRepository repository;

    @BeforeEach
    void setUp() {
        repository = new ProductMockRepository();
    }

    @Test
    @DisplayName("findAll returns preloaded initial items")
    void findAll_returnsInitialItems() {
        List<Product> products = repository.findAll();
        assertNotNull(products);
        assertEquals(3, products.size());
    }

    @Test
    @DisplayName("findById returns product when ID exists")
    void findById_returnsProduct_whenExists() {
        Optional<Product> product = repository.findById("1");
        assertTrue(product.isPresent());
        assertEquals("Anti-gravity Yarn Star", product.get().getName());
    }

    @Test
    @DisplayName("findById returns empty Optional when ID does not exist")
    void findById_returnsEmpty_whenMissing() {
        Optional<Product> product = repository.findById("non-existing-id");
        assertTrue(product.isEmpty());
    }

    @Test
    @DisplayName("save adds new product")
    void save_addsNewProduct() {
        Product newProduct = new Product("new-1", "Comet Collar", "Comfortable collar", new BigDecimal("15.00"), "Apparel");
        Product saved = repository.save(newProduct);

        assertEquals(newProduct, saved);
        assertEquals(4, repository.findAll().size());
        assertTrue(repository.findById("new-1").isPresent());
    }

    @Test
    @DisplayName("save updates existing product with same ID")
    void save_updatesExistingProduct() {
        Product updatedProduct = new Product("1", "Updated Yarn Star", "New desc", new BigDecimal("25.00"), "Toys");
        repository.save(updatedProduct);

        assertEquals(3, repository.findAll().size());
        Optional<Product> retrieved = repository.findById("1");
        assertTrue(retrieved.isPresent());
        assertEquals("Updated Yarn Star", retrieved.get().getName());
    }

    @Test
    @DisplayName("deleteById returns true and removes product when ID exists")
    void deleteById_removesProduct_whenExists() {
        boolean deleted = repository.deleteById("1");
        assertTrue(deleted);
        assertEquals(2, repository.findAll().size());
        assertTrue(repository.findById("1").isEmpty());
    }

    @Test
    @DisplayName("deleteById returns false when ID does not exist")
    void deleteById_returnsFalse_whenMissing() {
        boolean deleted = repository.deleteById("non-existing-id");
        assertFalse(deleted);
        assertEquals(3, repository.findAll().size());
    }
}
