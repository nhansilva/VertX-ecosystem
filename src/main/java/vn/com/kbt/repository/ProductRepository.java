package vn.com.kbt.repository;

import io.vertx.core.Future;
import vn.com.kbt.model.Product;

import java.util.List;

/**
 * Repository interface for Product CRUD operations.
 */
public interface ProductRepository {
    /**
     * Find all products.
     *
     * @return Future with a list of all products
     */
    Future<List<Product>> findAll();

    /**
     * Find a product by its ID.
     *
     * @param id the product ID
     * @return Future with the product if found, otherwise a failed future
     */
    Future<Product> findById(String id);

    /**
     * Save a new product.
     *
     * @param product the product to save
     * @return Future with the saved product
     */
    Future<Product> save(Product product);

    /**
     * Update an existing product.
     *
     * @param id the product ID
     * @param product the updated product data
     * @return Future with the updated product if found, otherwise a failed future
     */
    Future<Product> update(String id, Product product);

    /**
     * Delete a product by its ID.
     *
     * @param id the product ID
     * @return Future with true if deleted, otherwise a failed future
     */
    Future<Boolean> delete(String id);
}
