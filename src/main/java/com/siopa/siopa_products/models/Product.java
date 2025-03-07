package com.siopa.siopa_products.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

/**
 * Represents a product entity stored in the MongoDB database.
 */
@Data
@Document(collection = "products")
public class Product {

    /**
     * Unique identifier for the product.
     */
    @Id
    private String productId;

    /**
     * Identifier of the store that owns the product.
     */
    private String storeId;

    /**
     * Name of the product.
     */
    private String name;

    /**
     * Price of the product.
     */
    private double price;

    /**
     * Description of the product.
     */
    private String description;

    /**
     * Category of the product (e.g., electronics, clothing, groceries).
     */
    private String category;

    /**
     * Quantity of the product available in stock.
     */
    private int quantity;

    /**
     * Additional attributes of the product as key-value pairs.
     */
    private Map<String, Object> attributes;
}
