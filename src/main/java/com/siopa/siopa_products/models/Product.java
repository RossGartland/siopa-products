package com.siopa.siopa_products.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;
import java.util.UUID;

@Data
@Document(collection = "products")
public class Product {
    @Id
    private String productId;

    private String storeId;

    private String name;
    private double price;
    private String description;
    private String category;
    private int quantity;

    private Map<String, Object> attributes; //Additional fields
}
