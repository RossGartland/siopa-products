package com.siopa.siopa_products.controllers;

import com.siopa.siopa_products.dto.ProductRequest;
import com.siopa.siopa_products.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.siopa.siopa_products.services.ProductService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing product-related operations.
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    /**
     * Retrieves a list of all products.
     *
     * @return a list of all products.
     */
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    /**
     * Retrieves a product by its unique identifier.
     *
     * @param id the unique identifier of the product.
     * @return an {@link Optional} containing the product if found, otherwise empty.
     */
    @GetMapping("/{id}")
    public Optional<Product> getProductById(@PathVariable String id) {
        return productService.getProductById(id);
    }

    /**
     * Retrieves all products belonging to a specific store.
     *
     * @param storeId the unique identifier of the store.
     * @return a list of products associated with the specified store.
     */
    @GetMapping("/store/{storeId}")
    public List<Product> getProductsByStoreId(@PathVariable String storeId) {
        return productService.getProductsByStoreId(storeId);
    }

    /**
     * Creates a new product.
     *
     * @param productRequest the request body containing product details.
     * @return the created product.
     */
    @PostMapping
    public Product createProduct(@Valid @RequestBody ProductRequest productRequest) {
        return productService.createProduct(productRequest);
    }

    /**
     * Updates an existing product with new details.
     *
     * @param id the unique identifier of the product to be updated.
     * @param productRequest the request body containing updated product details.
     * @return the updated product.
     */
    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable String id, @Valid @RequestBody ProductRequest productRequest) {
        return productService.updateProduct(id, productRequest);
    }

    /**
     * Deletes a product by its unique identifier.
     *
     * @param id the unique identifier of the product to be deleted.
     */
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable String id) {
        productService.deleteProduct(id);
    }

    /**
     * Updates the quantity of a specific product.
     *
     * @param id the unique identifier of the product.
     * @param quantity the new quantity to be updated.
     * @return the updated product with the new quantity.
     */
    @PatchMapping("/{id}/quantity")
    public Product updateProductQuantity(@PathVariable String id, @RequestParam int quantity) {
        return productService.updateProductQuantity(id, quantity);
    }
}
