package com.siopa.siopa_products.services;

import com.siopa.siopa_products.dto.ProductRequest;
import com.siopa.siopa_products.models.Product;
import com.siopa.siopa_products.repositories.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing product-related operations.
 */
@Service
public class ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    private ProductRepository productRepository;

    /**
     * Retrieves all products from the database.
     *
     * @return a list of all products.
     */
    public List<Product> getAllProducts() {
        logger.info("Fetching all products");
        List<Product> products = productRepository.findAll();
        logger.debug("Retrieved {} products from database", products.size());
        return products;
    }

    /**
     * Retrieves a product by its unique identifier.
     *
     * @param id the unique identifier of the product.
     * @return an {@link Optional} containing the product if found, otherwise empty.
     */
    public Optional<Product> getProductById(String id) {
        logger.info("Fetching product with ID: {}", id);
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            logger.debug("Product found: {}", product.get());
        } else {
            logger.warn("Product with ID {} not found", id);
        }
        return product;
    }

    /**
     * Retrieves all products belonging to a specific store.
     *
     * @param storeId the unique identifier of the store.
     * @return a list of products associated with the specified store.
     */
    public List<Product> getProductsByStoreId(String storeId) {
        logger.info("Fetching products for Store ID: {}", storeId);
        List<Product> products = productRepository.findByStoreId(storeId);
        logger.debug("Found {} products for Store ID: {}", products.size(), storeId);
        return products;
    }

    /**
     * Creates a new product in the database.
     *
     * @param productRequest the request object containing product details.
     * @return the created product.
     */
    public Product createProduct(ProductRequest productRequest) {
        logger.info("Creating a new product: {}", productRequest.getName());

        Product product = new Product();
        product.setName(productRequest.getName());
        product.setPrice(productRequest.getPrice());
        product.setCategory(productRequest.getCategory());
        product.setDescription(productRequest.getDescription());
        product.setStoreId(productRequest.getStoreId());
        product.setQuantity(productRequest.getQuantity());
        product.setAttributes(productRequest.getAttributes());

        Product savedProduct = productRepository.save(product);
        logger.info("Product created successfully with ID: {}", savedProduct.getProductId());

        return savedProduct;
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
        logger.info("Updating product with ID: {}", id);

        return productRepository.findById(id).map(existingProduct -> {
            existingProduct.setName(productRequest.getName());
            existingProduct.setPrice(productRequest.getPrice());
            existingProduct.setCategory(productRequest.getCategory());
            existingProduct.setDescription(productRequest.getDescription());
            existingProduct.setStoreId(productRequest.getStoreId());
            existingProduct.setQuantity(productRequest.getQuantity());
            existingProduct.setAttributes(productRequest.getAttributes());

            Product updatedProduct = productRepository.save(existingProduct);
            logger.info("Product ID {} updated successfully", id);
            return updatedProduct;
        }).orElseThrow(() -> {
            logger.error("Product with ID {} not found for update", id);
            return new RuntimeException("Product not found");
        });
    }

    /**
     * Deletes a product by its unique identifier.
     *
     * @param id the unique identifier of the product to be deleted.
     */
    public void deleteProduct(String id) {
        logger.warn("Deleting product with ID: {}", id);
        productRepository.deleteById(id);
        logger.info("Product with ID {} deleted successfully", id);
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
        logger.info("Updating quantity for Product ID: {} to {}", productId, quantity);

        return productRepository.findById(productId).map(product -> {
            product.setQuantity(quantity);
            Product updatedProduct = productRepository.save(product);
            logger.info("Product ID {} quantity updated to {}", productId, quantity);
            return updatedProduct;
        }).orElseThrow(() -> {
            logger.error("Product with ID {} not found for quantity update", productId);
            return new RuntimeException("Product not found");
        });
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
        logger.info("Reducing quantity for Product ID: {} by {}", productId, quantity);

        return productRepository.findById(productId).map(product -> {
            if (product.getQuantity() < quantity) {
                logger.error("Insufficient stock for Product ID: {}. Available: {}, Requested: {}",
                        productId, product.getQuantity(), quantity);
                throw new RuntimeException("Insufficient stock for product: " + productId);
            }

            product.setQuantity(product.getQuantity() - quantity);
            Product updatedProduct = productRepository.save(product);
            logger.info("Product ID {} quantity reduced by {}. New quantity: {}", productId, quantity, product.getQuantity());
            return updatedProduct;
        }).orElseThrow(() -> {
            logger.error("Product with ID {} not found for quantity reduction", productId);
            return new RuntimeException("Product not found");
        });
    }
}
