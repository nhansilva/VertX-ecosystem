package vn.com.kbt.repository;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import vn.com.kbt.model.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory implementation of the ProductRepository interface.
 * This implementation stores products in a ConcurrentHashMap for thread safety.
 */
public class InMemoryProductRepository implements ProductRepository {

    private final Map<String, Product> products = new ConcurrentHashMap<>();

    @Override
    public Future<List<Product>> findAll() {
        Promise<List<Product>> promise = Promise.promise();
        try {
            List<Product> productList = new ArrayList<>(products.values());
            promise.complete(productList);
        } catch (Exception e) {
            promise.fail(e);
        }
        return promise.future();
    }

    @Override
    public Future<Product> findById(String id) {
        Promise<Product> promise = Promise.promise();
        try {
            Product product = products.get(id);
            if (product != null) {
                promise.complete(product);
            } else {
                promise.fail("Product not found with id: " + id);
            }
        } catch (Exception e) {
            promise.fail(e);
        }
        return promise.future();
    }

    @Override
    public Future<Product> save(Product product) {
        Promise<Product> promise = Promise.promise();
        try {
            // Generate a UUID if id is not provided
            if (product.getId() == null || product.getId().isEmpty()) {
                product.setId(UUID.randomUUID().toString());
            }
            products.put(product.getId(), product);
            promise.complete(product);
        } catch (Exception e) {
            promise.fail(e);
        }
        return promise.future();
    }

    @Override
    public Future<Product> update(String id, Product product) {
        Promise<Product> promise = Promise.promise();
        try {
            if (products.containsKey(id)) {
                // Ensure the ID in the path matches the product ID
                product.setId(id);
                products.put(id, product);
                promise.complete(product);
            } else {
                promise.fail("Product not found with id: " + id);
            }
        } catch (Exception e) {
            promise.fail(e);
        }
        return promise.future();
    }

    @Override
    public Future<Boolean> delete(String id) {
        Promise<Boolean> promise = Promise.promise();
        try {
            if (products.containsKey(id)) {
                products.remove(id);
                promise.complete(true);
            } else {
                promise.fail("Product not found with id: " + id);
            }
        } catch (Exception e) {
            promise.fail(e);
        }
        return promise.future();
    }
}
