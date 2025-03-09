package com.siopa.siopa_products.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.siopa.siopa_products.dto.ProductOrderMessage;
import com.siopa.siopa_products.services.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * Kafka Consumer that listens for product order messages and updates inventory.
 */
@Service
public class ProductConsumer {

    private static final Logger logger = LoggerFactory.getLogger(ProductConsumer.class);

    private final ProductService productService;
    private final ObjectMapper objectMapper;

    public ProductConsumer(ProductService productService, ObjectMapper objectMapper) {
        this.productService = productService;
        this.objectMapper = objectMapper;
    }

    /**
     * Listens for Kafka messages containing product ID and quantity in JSON format.
     *
     * @param message the JSON message containing product ID and quantity
     */
    @KafkaListener(topics = "${spring.kafka.topic.order}", groupId = "product-group")
    public void consumeOrder(String message) {
        logger.info("Consuming Kafka Message: {}", message);
        try {
            ProductOrderMessage orderMessage = objectMapper.readValue(message, ProductOrderMessage.class);
            logger.debug("Parsed ProductOrderMessage: productId={}, quantity={}",
                    orderMessage.getProductId(), orderMessage.getQuantity());
            productService.reduceProductQuantity(orderMessage.getProductId(), orderMessage.getQuantity());
            logger.info("Updated inventory for productId {} after consuming order message",
                    orderMessage.getProductId());
        } catch (JsonProcessingException e) {
            logger.error("Failed to parse product order message: {}", message, e);
            throw new RuntimeException("Failed to parse product order message", e);
        }
    }
}
