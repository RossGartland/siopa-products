package com.siopa.siopa_products.repositories;

import com.siopa.siopa_products.models.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
}
