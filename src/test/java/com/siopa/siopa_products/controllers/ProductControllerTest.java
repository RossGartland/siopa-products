package com.siopa.siopa_products.controllers;

import com.siopa.siopa_products.dto.ProductRequest;
import com.siopa.siopa_products.models.Product;
import com.siopa.siopa_products.services.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for {@link ProductController}.
 */
@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private Product product;
    private ProductRequest productRequest;
    private final String PRODUCT_ID = "123";
    private final String STORE_ID = "store123";

    /**
     * Sets up test data before each test case.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();

        product = new Product();
        product.setProductId(PRODUCT_ID);
        product.setStoreId(STORE_ID);
        product.setName("Test Product");
        product.setPrice(29.99);
        product.setCategory("Electronics");
        product.setDescription("Test Description");
        product.setQuantity(100);

        productRequest = new ProductRequest();
        productRequest.setName("Test Product");
        productRequest.setPrice(29.99);
        productRequest.setCategory("Electronics");
        productRequest.setDescription("Test Description");
        productRequest.setStoreId(STORE_ID);
        productRequest.setQuantity(100);
    }

    /**
     * Tests retrieving all products.
     */
    @Test
    void getAllProducts_ShouldReturnListOfProducts() throws Exception {
        List<Product> products = Arrays.asList(product);
        when(productService.getAllProducts()).thenReturn(products);

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(products.size()));

        verify(productService, times(1)).getAllProducts();
    }

    /**
     * Tests retrieving a product by ID.
     */
    @Test
    void getProductById_ShouldReturnProduct_WhenFound() throws Exception {
        when(productService.getProductById(PRODUCT_ID)).thenReturn(Optional.of(product));

        mockMvc.perform(get("/api/products/{id}", PRODUCT_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(product.getName()));

        verify(productService, times(1)).getProductById(PRODUCT_ID);
    }

    /**
     * Tests retrieving products by store ID.
     */
    @Test
    void getProductsByStoreId_ShouldReturnListOfProducts() throws Exception {
        List<Product> products = Arrays.asList(product);
        when(productService.getProductsByStoreId(STORE_ID)).thenReturn(products);

        mockMvc.perform(get("/api/products/store/{storeId}", STORE_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(products.size()));

        verify(productService, times(1)).getProductsByStoreId(STORE_ID);
    }

    /**
     * Tests creating a new product.
     */
    @Test
    void createProduct_ShouldReturnCreatedProduct() throws Exception {
        when(productService.createProduct(any(ProductRequest.class))).thenReturn(product);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"name\": \"Test Product\", \"price\": 29.99, \"category\": \"Electronics\", \"description\": \"Test Description\", \"storeId\": \"store123\", \"quantity\": 100 }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(product.getName()));

        verify(productService, times(1)).createProduct(any(ProductRequest.class));
    }

    /**
     * Tests updating an existing product.
     */
    @Test
    void updateProduct_ShouldReturnUpdatedProduct() throws Exception {
        when(productService.updateProduct(eq(PRODUCT_ID), any(ProductRequest.class))).thenReturn(product);

        mockMvc.perform(put("/api/products/{id}", PRODUCT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"name\": \"Updated Product\", \"price\": 39.99, \"category\": \"Electronics\", \"description\": \"Updated Description\", \"storeId\": \"store123\", \"quantity\": 50 }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(product.getName()));

        verify(productService, times(1)).updateProduct(eq(PRODUCT_ID), any(ProductRequest.class));
    }

    /**
     * Tests deleting a product.
     */
    @Test
    void deleteProduct_ShouldDeleteProduct() throws Exception {
        doNothing().when(productService).deleteProduct(PRODUCT_ID);

        mockMvc.perform(delete("/api/products/{id}", PRODUCT_ID))
                .andExpect(status().isOk());

        verify(productService, times(1)).deleteProduct(PRODUCT_ID);
    }

    /**
     * Tests updating a product's quantity.
     */
    @Test
    void updateProductQuantity_ShouldReturnUpdatedProduct() throws Exception {
        when(productService.updateProductQuantity(PRODUCT_ID, 50)).thenReturn(product);

        mockMvc.perform(patch("/api/products/{id}/quantity", PRODUCT_ID)
                        .param("quantity", "50"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(product.getName()));

        verify(productService, times(1)).updateProductQuantity(PRODUCT_ID, 50);
    }
}
