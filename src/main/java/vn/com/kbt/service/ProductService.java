package vn.com.kbt.service;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import vn.com.kbt.model.Product;
import vn.com.kbt.repository.ProductRepository;

import java.util.List;

/**
 * Service class for handling product-related business logic.
 */
public class ProductService {

    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    /**
     * Get all products.
     *
     * @return Future with a list of all products
     */
    public Future<List<Product>> getAllProducts() {
        return repository.findAll();
    }

    /**
     * Get a product by its ID.
     *
     * @param id the product ID
     * @return Future with the product if found
     */
    public Future<Product> getProductById(String id) {
        return repository.findById(id);
    }

    /**
     * Create a new product.
     *
     * @param json the product data as JsonObject
     * @return Future with the created product
     */
    public Future<Product> createProduct(JsonObject json) {
        Product product = json.mapTo(Product.class);
        return repository.save(product);
    }

    /**
     * Update an existing product.
     *
     * @param id the product ID
     * @param json the updated product data as JsonObject
     * @return Future with the updated product
     */
    public Future<Product> updateProduct(String id, JsonObject json) {
        Product product = json.mapTo(Product.class);
        return repository.update(id, product);
    }

    /**
     * Delete a product by its ID.
     *
     * @param id the product ID
     * @return Future with true if deleted
     */
    public Future<Boolean> deleteProduct(String id) {
        return repository.delete(id);
    }
}
