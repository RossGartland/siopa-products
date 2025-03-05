package com.siopa.siopa_products.controllers;

import com.siopa.siopa_products.dto.ProductRequest;
import com.siopa.siopa_products.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.siopa.siopa_products.services.ProductService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public Optional<Product> getProductById(@PathVariable String id) {
        return productService.getProductById(id);
    }

    @GetMapping("/store/{storeId}")
    public List<Product> getProductsByStoreId(@PathVariable String storeId) {
        return productService.getProductsByStoreId(storeId);
    }

    @PostMapping
    public Product createProduct(@Valid @RequestBody ProductRequest productRequest) {
        return productService.createProduct(productRequest);
    }

    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable String id, @Valid @RequestBody ProductRequest productRequest) {
        return productService.updateProduct(id, productRequest);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable String id) {
        productService.deleteProduct(id);
    }

    @PatchMapping("/{id}/quantity")
    public Product updateProductQuantity(@PathVariable String id, @RequestParam int quantity) {
        return productService.updateProductQuantity(id, quantity);
    }
}
