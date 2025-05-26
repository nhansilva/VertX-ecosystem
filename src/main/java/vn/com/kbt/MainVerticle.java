package vn.com.kbt;

import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.ext.web.Router;
import io.vertx.core.http.HttpServer;
import vn.com.kbt.handler.ProductHandler;
import vn.com.kbt.repository.InMemoryProductRepository;
import vn.com.kbt.repository.ProductRepository;
import vn.com.kbt.service.ProductService;

public class MainVerticle extends VerticleBase {

  @Override
  public Future<?> start() {
    // Create the router
    Router router = Router.router(vertx);

    // Set up the repository, service, and handler
    ProductRepository productRepository = new InMemoryProductRepository();
    ProductService productService = new ProductService(productRepository);
    ProductHandler productHandler = new ProductHandler(productService);

    // Set up the API routes
    productHandler.setupRoutes(vertx, router);

    // Add a simple root endpoint
    router.get("/").handler(ctx -> {
      ctx.response()
        .putHeader("content-type", "text/plain")
        .end("Welcome to the Product API. Use /api/products to access the API.");
    });

    // Create and start the HTTP server
    return vertx.createHttpServer()
      .requestHandler(router)
      .listen(8888)
      .onSuccess(http -> {
        System.out.println("HTTP server started on port 8888");
        System.out.println("Product API available at http://localhost:8888/api/products");
      });
  }
}
