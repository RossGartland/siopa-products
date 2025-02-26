package com.siopa.siopa_products.services;

import com.siopa.siopa_products.dto.ProductRequest;
import com.siopa.siopa_products.models.Product;
import com.siopa.siopa_products.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(String id) {
        return productRepository.findById(id);
    }

    public Product createProduct(ProductRequest productRequest) {
        Product product = new Product();
        product.setName(productRequest.getName());
        product.setPrice(productRequest.getPrice());
        product.setCategory(productRequest.getCategory());
        product.setStoreId(productRequest.getStoreId());
        product.setQuantity(productRequest.getQuantity());
        product.setAttributes(productRequest.getAttributes());
        return productRepository.save(product);
    }

    public Product updateProduct(String id, ProductRequest productRequest) {
        return productRepository.findById(id).map(existingProduct -> {
            existingProduct.setName(productRequest.getName());
            existingProduct.setPrice(productRequest.getPrice());
            existingProduct.setCategory(productRequest.getCategory());
            existingProduct.setStoreId(productRequest.getStoreId());
            existingProduct.setQuantity(productRequest.getQuantity());
            existingProduct.setAttributes(productRequest.getAttributes());
            return productRepository.save(existingProduct);
        }).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public void deleteProduct(String id) {
        productRepository.deleteById(id);
    }

    public Product updateProductQuantity(String productId, int quantity) {
        return productRepository.findById(productId).map(product -> {
            product.setQuantity(quantity);
            return productRepository.save(product);
        }).orElseThrow(() -> new RuntimeException("Product not found"));
    }
    public Product reduceProductQuantity(String productId, int quantity) {
        return productRepository.findById(productId).map(product -> {
            if (product.getQuantity() < quantity) {
                throw new RuntimeException("Insufficient stock for product: " + productId);
            }
            product.setQuantity(product.getQuantity() - quantity);
            return productRepository.save(product);
        }).orElseThrow(() -> new RuntimeException("Product not found"));
    }
}