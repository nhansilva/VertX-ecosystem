package vn.com.kbt.handler;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import vn.com.kbt.service.ProductService;

/**
 * Handler for product-related HTTP endpoints.
 */
public class ProductHandler {

    private final ProductService productService;

    public ProductHandler(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Set up the routes for the product API.
     *
     * @param vertx the Vertx instance
     * @param router the Router instance
     * @return the updated Router
     */
    public Router setupRoutes(Vertx vertx, Router router) {
        // Enable request body parsing
        router.route("/api/products*").handler(BodyHandler.create());

        // Get all products
        router.get("/api/products").handler(this::getAllProducts);

        // Get product by ID
        router.get("/api/products/:id").handler(this::getProductById);

        // Create new product
        router.post("/api/products").handler(this::createProduct);

        // Update product
        router.put("/api/products/:id").handler(this::updateProduct);

        // Delete product
        router.delete("/api/products/:id").handler(this::deleteProduct);

        return router;
    }

    private void getAllProducts(RoutingContext context) {
        HttpServerResponse response = context.response();
        response.putHeader("content-type", "application/json");

        productService.getAllProducts()
            .onSuccess(products -> {
                response.setStatusCode(200).end(Json.encodePrettily(products));
            })
            .onFailure(err -> {
                response.setStatusCode(500).end(new JsonObject()
                    .put("error", err.getMessage())
                    .encodePrettily());
            });
    }

    private void getProductById(RoutingContext context) {
        String id = context.pathParam("id");
        HttpServerResponse response = context.response();
        response.putHeader("content-type", "application/json");

        productService.getProductById(id)
            .onSuccess(product -> {
                response.setStatusCode(200).end(Json.encodePrettily(product));
            })
            .onFailure(err -> {
                response.setStatusCode(404).end(new JsonObject()
                    .put("error", "Product not found with id: " + id)
                    .encodePrettily());
            });
    }

    private void createProduct(RoutingContext context) {
        HttpServerResponse response = context.response();
        response.putHeader("content-type", "application/json");

        try {
            JsonObject json = context.getBodyAsJson();
            productService.createProduct(json)
                .onSuccess(product -> {
                    response.setStatusCode(201).end(Json.encodePrettily(product));
                })
                .onFailure(err -> {
                    response.setStatusCode(400).end(new JsonObject()
                        .put("error", err.getMessage())
                        .encodePrettily());
                });
        } catch (Exception e) {
            response.setStatusCode(400).end(new JsonObject()
                .put("error", "Invalid JSON format")
                .encodePrettily());
        }
    }

    private void updateProduct(RoutingContext context) {
        String id = context.pathParam("id");
        HttpServerResponse response = context.response();
        response.putHeader("content-type", "application/json");

        try {
            JsonObject json = context.getBodyAsJson();
            productService.updateProduct(id, json)
                .onSuccess(product -> {
                    response.setStatusCode(200).end(Json.encodePrettily(product));
                })
                .onFailure(err -> {
                    response.setStatusCode(404).end(new JsonObject()
                        .put("error", "Product not found with id: " + id)
                        .encodePrettily());
                });
        } catch (Exception e) {
            response.setStatusCode(400).end(new JsonObject()
                .put("error", "Invalid JSON format")
                .encodePrettily());
        }
    }

    private void deleteProduct(RoutingContext context) {
        String id = context.pathParam("id");
        HttpServerResponse response = context.response();
        response.putHeader("content-type", "application/json");

        productService.deleteProduct(id)
            .onSuccess(result -> {
                response.setStatusCode(204).end();
            })
            .onFailure(err -> {
                response.setStatusCode(404).end(new JsonObject()
                    .put("error", "Product not found with id: " + id)
                    .encodePrettily());
            });
    }
}
