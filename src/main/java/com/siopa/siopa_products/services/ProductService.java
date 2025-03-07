package com.siopa.siopa_products.services;

import com.siopa.siopa_products.dto.ProductRequest;
import com.siopa.siopa_products.models.Product;
import com.siopa.siopa_products.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing product-related operations.
 */
@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    /**
     * Retrieves all products from the database.
     *
     * @return a list of all products.
     */
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    /**
     * Retrieves a product by its unique identifier.
     *
     * @param id the unique identifier of the product.
     * @return an {@link Optional} containing the product if found, otherwise empty.
     */
    public Optional<Product> getProductById(String id) {
        return productRepository.findById(id);
    }

    /**
     * Retrieves all products belonging to a specific store.
     *
     * @param storeId the unique identifier of the store.
     * @return a list of products associated with the specified store.
     */
    public List<Product> getProductsByStoreId(String storeId) {
        return productRepository.findByStoreId(storeId);
    }

    /**
     * Creates a new product in the database.
     *
     * @param productRequest the request object containing product details.
     * @return the created product.
     */
    public Product createProduct(ProductRequest productRequest) {
        Product product = new Product();
        product.setName(productRequest.getName());
        product.setPrice(productRequest.getPrice());
        product.setCategory(productRequest.getCategory());
        product.setDescription(productRequest.getDescription());
        product.setStoreId(productRequest.getStoreId());
        product.setQuantity(productRequest.getQuantity());
        product.setAttributes(productRequest.getAttributes());
        return productRepository.save(product);
    }

    /**
     * Updates an existing product with new details.
     *
     * @param id the unique identifier of the product to be updated.
     * @param productRequest the request object containing updated product details.
     * @return the updated product.
     * @throws RuntimeException if the product is not found.
     */
    public Product updateProduct(String id, ProductRequest productRequest) {
        return productRepository.findById(id).map(existingProduct -> {
            existingProduct.setName(productRequest.getName());
            existingProduct.setPrice(productRequest.getPrice());
            existingProduct.setCategory(productRequest.getCategory());
            existingProduct.setDescription(productRequest.getDescription());
            existingProduct.setStoreId(productRequest.getStoreId());
            existingProduct.setQuantity(productRequest.getQuantity());
            existingProduct.setAttributes(productRequest.getAttributes());
            return productRepository.save(existingProduct);
        }).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    /**
     * Deletes a product by its unique identifier.
     *
     * @param id the unique identifier of the product to be deleted.
     */
    public void deleteProduct(String id) {
        productRepository.deleteById(id);
    }

    /**
     * Updates the quantity of a product.
     *
     * @param productId the unique identifier of the product.
     * @param quantity the new quantity to be updated.
     * @return the updated product with the new quantity.
     * @throws RuntimeException if the product is not found.
     */
    public Product updateProductQuantity(String productId, int quantity) {
        return productRepository.findById(productId).map(product -> {
            product.setQuantity(quantity);
            return productRepository.save(product);
        }).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    /**
     * Reduces the quantity of a product when an item is purchased or reserved.
     *
     * @param productId the unique identifier of the product.
     * @param quantity the amount to reduce from the current stock.
     * @return the updated product after reducing the quantity.
     * @throws RuntimeException if the product is not found or if there is insufficient stock.
     */
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
