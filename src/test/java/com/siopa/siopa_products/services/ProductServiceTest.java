package com.siopa.siopa_products.services;

import com.siopa.siopa_products.dto.ProductRequest;
import com.siopa.siopa_products.models.Product;
import com.siopa.siopa_products.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link ProductService}.
 */
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

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
    void getAllProducts_ShouldReturnListOfProducts() {
        when(productRepository.findAll()).thenReturn(Arrays.asList(product));

        List<Product> result = productService.getAllProducts();

        assertEquals(1, result.size());
        verify(productRepository, times(1)).findAll();
    }

    /**
     * Tests retrieving a product by ID.
     */
    @Test
    void getProductById_ShouldReturnProduct_WhenFound() {
        when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.of(product));

        Optional<Product> result = productService.getProductById(PRODUCT_ID);

        assertTrue(result.isPresent());
        assertEquals(product.getName(), result.get().getName());
        verify(productRepository, times(1)).findById(PRODUCT_ID);
    }

    /**
     * Tests retrieving products by store ID.
     */
    @Test
    void getProductsByStoreId_ShouldReturnListOfProducts() {
        when(productRepository.findByStoreId(STORE_ID)).thenReturn(Arrays.asList(product));

        List<Product> result = productService.getProductsByStoreId(STORE_ID);

        assertEquals(1, result.size());
        verify(productRepository, times(1)).findByStoreId(STORE_ID);
    }

    /**
     * Tests creating a new product.
     */
    @Test
    void createProduct_ShouldReturnCreatedProduct() {
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product result = productService.createProduct(productRequest);

        assertNotNull(result);
        assertEquals(productRequest.getName(), result.getName());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    /**
     * Tests updating an existing product.
     */
    @Test
    void updateProduct_ShouldReturnUpdatedProduct_WhenProductExists() {
        when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product result = productService.updateProduct(PRODUCT_ID, productRequest);

        assertNotNull(result);
        assertEquals(productRequest.getName(), result.getName());
        verify(productRepository, times(1)).findById(PRODUCT_ID);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    /**
     * Tests updating a product that does not exist.
     */
    @Test
    void updateProduct_ShouldThrowException_WhenProductNotFound() {
        when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            productService.updateProduct(PRODUCT_ID, productRequest);
        });

        assertEquals("Product not found", exception.getMessage());
        verify(productRepository, times(1)).findById(PRODUCT_ID);
    }

    /**
     * Tests deleting a product.
     */
    @Test
    void deleteProduct_ShouldDeleteProduct_WhenProductExists() {
        doNothing().when(productRepository).deleteById(PRODUCT_ID);

        productService.deleteProduct(PRODUCT_ID);

        verify(productRepository, times(1)).deleteById(PRODUCT_ID);
    }

    /**
     * Tests updating a product's quantity.
     */
    @Test
    void updateProductQuantity_ShouldReturnUpdatedProduct() {
        when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product result = productService.updateProductQuantity(PRODUCT_ID, 50);

        assertEquals(50, result.getQuantity());
        verify(productRepository, times(1)).findById(PRODUCT_ID);
        verify(productRepository, times(1)).save(product);
    }

    /**
     * Tests reducing a product's quantity.
     */
    @Test
    void reduceProductQuantity_ShouldReduceQuantity_WhenSufficientStock() {
        when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product result = productService.reduceProductQuantity(PRODUCT_ID, 10);

        assertEquals(90, result.getQuantity());
        verify(productRepository, times(1)).findById(PRODUCT_ID);
        verify(productRepository, times(1)).save(product);
    }

    /**
     * Tests reducing a product's quantity when stock is insufficient.
     */
    @Test
    void reduceProductQuantity_ShouldThrowException_WhenInsufficientStock() {
        when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.of(product));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            productService.reduceProductQuantity(PRODUCT_ID, 200);
        });

        assertEquals("Insufficient stock for product: " + PRODUCT_ID, exception.getMessage());
        verify(productRepository, times(1)).findById(PRODUCT_ID);
    }

    /**
     * Tests reducing a product's quantity when the product is not found.
     */
    @Test
    void reduceProductQuantity_ShouldThrowException_WhenProductNotFound() {
        when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            productService.reduceProductQuantity(PRODUCT_ID, 10);
        });

        assertEquals("Product not found", exception.getMessage());
        verify(productRepository, times(1)).findById(PRODUCT_ID);
    }
}
