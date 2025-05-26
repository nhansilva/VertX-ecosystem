package vn.com.kbt;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;

/**
 * Main Verticle for the application.
 * This verticle is responsible for deploying all other verticles in the application.
 */
public class AppMainVerticle extends AbstractVerticle {

    @Override
    public void start(Promise<Void> startPromise) {
        System.out.println("Starting AppMainVerticle...");

        // Deploy the MainVerticle (API Verticle)
        deployApiVerticle()
            .onSuccess(id -> {
                System.out.println("API Verticle deployed successfully with ID: " + id);
                startPromise.complete();
            })
            .onFailure(err -> {
                System.err.println("Failed to deploy API Verticle: " + err.getMessage());
                startPromise.fail(err);
            });
    }

    /**
     * Deploy the API Verticle (MainVerticle).
     *
     * @return Future with the deployment ID
     */
    private Future<String> deployApiVerticle() {
        DeploymentOptions options = new DeploymentOptions()
            .setInstances(Runtime.getRuntime().availableProcessors());

        return vertx.deployVerticle(MainVerticle.class.getName(), options);
    }

    /**
     * Main method to deploy the AppMainVerticle directly.
     * This can be used for development and testing.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();

        vertx.deployVerticle(new AppMainVerticle())
            .onSuccess(id -> {
                System.out.println("AppMainVerticle deployed successfully with ID: " + id);
                System.out.println("Application started successfully!");
            })
            .onFailure(err -> {
                System.err.println("Failed to deploy AppMainVerticle: " + err.getMessage());
                err.printStackTrace();
                vertx.close();
            });
    }
}
