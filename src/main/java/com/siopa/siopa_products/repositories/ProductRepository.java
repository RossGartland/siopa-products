package com.siopa.siopa_products.repositories;

import com.siopa.siopa_products.models.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for performing CRUD operations on the {@link Product} collection in MongoDB.
 */
@Repository
public interface ProductRepository extends MongoRepository<Product, String> {

    /**
     * Finds all products associated with a specific store.
     *
     * @param storeId the unique identifier of the store.
     * @return a list of products belonging to the specified store.
     */
    List<Product> findByStoreId(String storeId);
}
