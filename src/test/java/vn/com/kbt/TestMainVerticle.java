package vn.com.kbt;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(VertxExtension.class)
public class TestMainVerticle {

  private WebClient webClient;

  @BeforeEach
  void deploy_verticle(Vertx vertx, VertxTestContext testContext) {
    webClient = WebClient.create(vertx, new WebClientOptions().setDefaultPort(8888).setDefaultHost("localhost"));
    vertx.deployVerticle(new MainVerticle()).onComplete(testContext.succeeding(id -> testContext.completeNow()));
  }

  @Test
  @DisplayName("Test server is up and running")
  void verticle_deployed(Vertx vertx, VertxTestContext testContext) {
    webClient.get("/")
      .send()
      .onComplete(testContext.succeeding(response -> {
        assertEquals(200, response.statusCode());
        assertTrue(response.bodyAsString().contains("Welcome to the Product API"));
        testContext.completeNow();
      }));
  }

  @Test
  @DisplayName("Test create product")
  void test_create_product(Vertx vertx, VertxTestContext testContext) {
    JsonObject product = new JsonObject()
      .put("name", "Test Product")
      .put("description", "A test product")
      .put("price", 19.99)
      .put("quantity", 10);

    webClient.post("/api/products")
      .sendJsonObject(product)
      .onComplete(testContext.succeeding(response -> {
        assertEquals(201, response.statusCode());
        JsonObject createdProduct = response.bodyAsJsonObject();
        assertNotNull(createdProduct.getString("id"));
        assertEquals("Test Product", createdProduct.getString("name"));
        assertEquals("A test product", createdProduct.getString("description"));
        assertEquals(19.99, createdProduct.getDouble("price"));
        assertEquals(10, createdProduct.getInteger("quantity"));

        // Store the product ID for later tests
        String productId = createdProduct.getString("id");

        // Now test getting the product by ID
        webClient.get("/api/products/" + productId)
          .send()
          .onComplete(testContext.succeeding(getResponse -> {
            assertEquals(200, getResponse.statusCode());
            JsonObject retrievedProduct = getResponse.bodyAsJsonObject();
            assertEquals(productId, retrievedProduct.getString("id"));
            assertEquals("Test Product", retrievedProduct.getString("name"));
            testContext.completeNow();
          }));
      }));
  }

  @Test
  @DisplayName("Test get all products")
  void test_get_all_products(Vertx vertx, VertxTestContext testContext) {
    // First create a product
    JsonObject product = new JsonObject()
      .put("name", "Another Product")
      .put("description", "Another test product")
      .put("price", 29.99)
      .put("quantity", 5);

    webClient.post("/api/products")
      .sendJsonObject(product)
      .onComplete(testContext.succeeding(createResponse -> {
        assertEquals(201, createResponse.statusCode());

        // Now get all products
        webClient.get("/api/products")
          .send()
          .onComplete(testContext.succeeding(getResponse -> {
            assertEquals(200, getResponse.statusCode());
            JsonArray products = getResponse.bodyAsJsonArray();
            assertTrue(products.size() > 0);
            testContext.completeNow();
          }));
      }));
  }

  @Test
  @DisplayName("Test update product")
  void test_update_product(Vertx vertx, VertxTestContext testContext) {
    // First create a product
    JsonObject product = new JsonObject()
      .put("name", "Product to Update")
      .put("description", "This product will be updated")
      .put("price", 39.99)
      .put("quantity", 15);

    webClient.post("/api/products")
      .sendJsonObject(product)
      .onComplete(testContext.succeeding(createResponse -> {
        assertEquals(201, createResponse.statusCode());
        JsonObject createdProduct = createResponse.bodyAsJsonObject();
        String productId = createdProduct.getString("id");

        // Now update the product
        JsonObject updatedProduct = new JsonObject()
          .put("name", "Updated Product")
          .put("description", "This product has been updated")
          .put("price", 49.99)
          .put("quantity", 20);

        webClient.put("/api/products/" + productId)
          .sendJsonObject(updatedProduct)
          .onComplete(testContext.succeeding(updateResponse -> {
            assertEquals(200, updateResponse.statusCode());
            JsonObject result = updateResponse.bodyAsJsonObject();
            assertEquals(productId, result.getString("id"));
            assertEquals("Updated Product", result.getString("name"));
            assertEquals("This product has been updated", result.getString("description"));
            assertEquals(49.99, result.getDouble("price"));
            assertEquals(20, result.getInteger("quantity"));
            testContext.completeNow();
          }));
      }));
  }

  @Test
  @DisplayName("Test delete product")
  void test_delete_product(Vertx vertx, VertxTestContext testContext) {
    // First create a product
    JsonObject product = new JsonObject()
      .put("name", "Product to Delete")
      .put("description", "This product will be deleted")
      .put("price", 59.99)
      .put("quantity", 25);

    webClient.post("/api/products")
      .sendJsonObject(product)
      .onComplete(testContext.succeeding(createResponse -> {
        assertEquals(201, createResponse.statusCode());
        JsonObject createdProduct = createResponse.bodyAsJsonObject();
        String productId = createdProduct.getString("id");

        // Now delete the product
        webClient.delete("/api/products/" + productId)
          .send()
          .onComplete(testContext.succeeding(deleteResponse -> {
            assertEquals(204, deleteResponse.statusCode());

            // Verify the product is deleted
            webClient.get("/api/products/" + productId)
              .send()
              .onComplete(testContext.succeeding(getResponse -> {
                assertEquals(404, getResponse.statusCode());
                testContext.completeNow();
              }));
          }));
      }));
  }
}
