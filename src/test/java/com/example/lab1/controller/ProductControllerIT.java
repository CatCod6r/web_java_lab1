package com.example.lab1.controller;

import com.example.lab1.dto.CreateProductRequest;
import com.example.lab1.dto.PagedResponse;
import com.example.lab1.dto.ProductResponseDto;
import com.example.lab1.exception.GlobalExceptionHandler;
import com.example.lab1.exception.ProductNotFoundException;
import com.example.lab1.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProductController Integration / Validation Tests")
class ProductControllerIT {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        mockMvc = MockMvcBuilders.standaloneSetup(productController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setValidator(validator)
                .build();
    }

    @Nested
    @DisplayName("GET /api/products Tests")
    class GetAllTests {

        @Test
        @DisplayName("Returns 200 OK and paged response")
        void getAll_returnsOkWithPagedResponse() throws Exception {
            ProductResponseDto dto = new ProductResponseDto("p-1", "Star Bow", "Shiny", new BigDecimal("10.00"), "Toys");
            PagedResponse<ProductResponseDto> pagedResponse = new PagedResponse<>(List.of(dto), 0, 10, 1);

            when(productService.getPagedProducts(0, 10)).thenReturn(pagedResponse);

            mockMvc.perform(get("/api/products")
                            .param("page", "0")
                            .param("size", "10")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.page", is(0)))
                    .andExpect(jsonPath("$.size", is(10)))
                    .andExpect(jsonPath("$.totalElements", is(1)))
                    .andExpect(jsonPath("$.content[0].id", is("p-1")))
                    .andExpect(jsonPath("$.content[0].name", is("Star Bow")));

            verify(productService).getPagedProducts(0, 10);
        }
    }

    @Nested
    @DisplayName("GET /api/products/{id} Tests")
    class GetByIdTests {

        @Test
        @DisplayName("Positive: Returns 200 OK when product exists")
        void getById_returnsOk_whenProductExists() throws Exception {
            ProductResponseDto dto = new ProductResponseDto("p-1", "Galaxy Laser", "Fun", new BigDecimal("15.50"), "Toys");
            when(productService.getById("p-1")).thenReturn(dto);

            mockMvc.perform(get("/api/products/p-1")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is("p-1")))
                    .andExpect(jsonPath("$.name", is("Galaxy Laser")));

            verify(productService).getById("p-1");
        }

        @Test
        @DisplayName("Negative: Returns 404 NOT FOUND when product does not exist")
        void getById_returnsNotFound_whenMissing() throws Exception {
            when(productService.getById("unknown-id")).thenThrow(new ProductNotFoundException("unknown-id"));

            mockMvc.perform(get("/api/products/unknown-id")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.title", is("Resource Not Found")))
                    .andExpect(jsonPath("$.status", is(404)))
                    .andExpect(jsonPath("$.type", is("https://cosmo-cats.market/errors/not-found")))
                    .andExpect(jsonPath("$.detail", containsString("unknown-id")));

            verify(productService).getById("unknown-id");
        }
    }

    @Nested
    @DisplayName("POST /api/products Validation Tests")
    class CreateProductValidationTests {

        @Test
        @DisplayName("Positive: Returns 201 CREATED when request is valid")
        void create_returnsCreated_whenValidRequest() throws Exception {
            String requestJson = """
                    {
                        "name": "Galaxy Wand Star",
                        "description": "Cosmic feather wand",
                        "price": 29.99,
                        "category": "Toys"
                    }
                    """;

            ProductResponseDto createdDto = new ProductResponseDto(
                    "gen-123",
                    "Galaxy Wand Star",
                    "Cosmic feather wand",
                    new BigDecimal("29.99"),
                    "Toys"
            );

            when(productService.create(any(CreateProductRequest.class))).thenReturn(createdDto);

            mockMvc.perform(post("/api/products")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andExpect(status().isCreated())
                    .andExpect(header().string("Location", containsString("/api/products/gen-123")))
                    .andExpect(jsonPath("$.id", is("gen-123")))
                    .andExpect(jsonPath("$.name", is("Galaxy Wand Star")))
                    .andExpect(jsonPath("$.category", is("Toys")));

            verify(productService).create(any(CreateProductRequest.class));
        }

        @Test
        @DisplayName("Negative: Returns 400 BAD REQUEST when name is blank")
        void create_returnsBadRequest_whenNameIsBlank() throws Exception {
            String requestJson = """
                    {
                        "name": "",
                        "price": 10.00,
                        "category": "Toys"
                    }
                    """;

            mockMvc.perform(post("/api/products")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.title", is("Validation failed")))
                    .andExpect(jsonPath("$.status", is(400)))
                    .andExpect(jsonPath("$.type", is("https://cosmo-cats.market/errors/validation")))
                    .andExpect(jsonPath("$.detail", containsString("Product name must not be blank")));
        }

        @Test
        @DisplayName("Negative: Returns 400 BAD REQUEST when name does not contain cosmic word")
        void create_returnsBadRequest_whenNameLacksCosmicWord() throws Exception {
            String requestJson = """
                    {
                        "name": "Ordinary Wooden Stick",
                        "price": 10.00,
                        "category": "Toys"
                    }
                    """;

            mockMvc.perform(post("/api/products")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.title", is("Validation failed")))
                    .andExpect(jsonPath("$.status", is(400)))
                    .andExpect(jsonPath("$.detail", containsString("Name must contain a cosmic word")));
        }

        @Test
        @DisplayName("Negative: Returns 400 BAD REQUEST when price is null")
        void create_returnsBadRequest_whenPriceIsNull() throws Exception {
            String requestJson = """
                    {
                        "name": "Moon Biscuit",
                        "price": null,
                        "category": "Food"
                    }
                    """;

            mockMvc.perform(post("/api/products")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.title", is("Validation failed")))
                    .andExpect(jsonPath("$.detail", containsString("Price is required")));
        }

        @Test
        @DisplayName("Negative: Returns 400 BAD REQUEST when price is zero or negative")
        void create_returnsBadRequest_whenPriceIsNegative() throws Exception {
            String requestJson = """
                    {
                        "name": "Orbit Ball",
                        "price": -5.00,
                        "category": "Toys"
                    }
                    """;

            mockMvc.perform(post("/api/products")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.title", is("Validation failed")))
                    .andExpect(jsonPath("$.detail", containsString("Field price must be greater than 0.")));
        }

        @Test
        @DisplayName("Negative: Returns 400 BAD REQUEST when category is blank")
        void create_returnsBadRequest_whenCategoryIsBlank() throws Exception {
            String requestJson = """
                    {
                        "name": "Comet Chew Toy",
                        "price": 8.50,
                        "category": "   "
                    }
                    """;

            mockMvc.perform(post("/api/products")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.title", is("Validation failed")))
                    .andExpect(jsonPath("$.detail", containsString("Category must not be blank")));
        }
    }

    @Nested
    @DisplayName("DELETE /api/products/{id} Tests")
    class DeleteProductTests {

        @Test
        @DisplayName("Positive: Returns 204 NO CONTENT when product deleted")
        void delete_returnsNoContent_whenProductExists() throws Exception {
            doNothing().when(productService).delete("del-1");

            mockMvc.perform(delete("/api/products/del-1"))
                    .andExpect(status().isNoContent());

            verify(productService).delete("del-1");
        }

        @Test
        @DisplayName("Negative: Returns 404 NOT FOUND when product to delete does not exist")
        void delete_returnsNotFound_whenMissing() throws Exception {
            doThrow(new ProductNotFoundException("del-missing")).when(productService).delete("del-missing");

            mockMvc.perform(delete("/api/products/del-missing"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.title", is("Resource Not Found")))
                    .andExpect(jsonPath("$.status", is(404)))
                    .andExpect(jsonPath("$.detail", containsString("del-missing")));

            verify(productService).delete("del-missing");
        }
    }
}
