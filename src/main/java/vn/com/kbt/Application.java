package vn.com.kbt;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.DeploymentOptions;

/**
 * Main application class that serves as the entry point for the application.
 * This class is responsible for deploying the MainVerticle.
 */
public class Application {

    /**
     * Main method that starts the application.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        // Create Vertx instance with default options
        Vertx vertx = Vertx.vertx(new VertxOptions());

        // Configure deployment options
        DeploymentOptions options = new DeploymentOptions();

        // Deploy the AppMainVerticle
        vertx.deployVerticle(AppMainVerticle.class.getName(), options)
            .onSuccess(id -> {
                System.out.println("AppMainVerticle deployed successfully with ID: " + id);
                System.out.println("Application started successfully!");
                System.out.println("API available at http://localhost:8888/api/products");
            })
            .onFailure(err -> {
                System.err.println("Failed to deploy AppMainVerticle: " + err.getMessage());
                err.printStackTrace();
                // Shutdown Vertx on deployment failure
                vertx.close();
            });
    }
}
