package com.siopa.siopa_products.DTO;

import lombok.Data;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.UUID;

@Data
public class ProductRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Price is required")
    @Min(value = 0, message = "Price must be a positive number")
    private Double price;

    @NotBlank(message = "Category is required")
    private String category;

    @NotBlank(message = "Store is required")
    private String storeId;

    private int quantity;

    private Map<String, Object> attributes;
}