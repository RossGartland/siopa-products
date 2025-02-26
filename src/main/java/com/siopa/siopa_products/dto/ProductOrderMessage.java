package com.siopa.siopa_products.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the product order message received from Kafka.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductOrderMessage {
    private String productId;
    private int quantity;
}