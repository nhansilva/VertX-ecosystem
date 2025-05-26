# Vert.x CRUD API

This project implements a simple CRUD (Create, Read, Update, Delete) API using Vert.x, a toolkit for building reactive applications on the JVM.

## Overview

The API provides endpoints for managing products with the following operations:
- Create a new product
- Retrieve a product by ID
- Retrieve all products
- Update an existing product
- Delete a product

## API Endpoints

### Get all products
```
GET /api/products
```
Returns a list of all products.

### Get a product by ID
```
GET /api/products/:id
```
Returns a single product with the specified ID.

### Create a new product
```
POST /api/products
```
Creates a new product. The request body should be a JSON object with the following properties:
```json
{
  "name": "Product Name",
  "description": "Product Description",
  "price": 19.99,
  "quantity": 10
}
```

### Update a product
```
PUT /api/products/:id
```
Updates an existing product with the specified ID. The request body should be a JSON object with the properties to update:
```json
{
  "name": "Updated Product Name",
  "description": "Updated Product Description",
  "price": 29.99,
  "quantity": 20
}
```

### Delete a product
```
DELETE /api/products/:id
```
Deletes the product with the specified ID.

## Running the Application

To run the application:

1. Clone the repository
2. Navigate to the project directory
3. Run the following command:
```
mvn clean compile exec:java
```

The server will start on port 8888. You can access the API at http://localhost:8888/api/products.

## Project Structure

The project follows a layered architecture:

- **Model**: Defines the data structure for products
- **Repository**: Handles data access operations
- **Service**: Contains business logic
- **Handler**: Manages HTTP requests and responses
- **MainVerticle**: Implements the HTTP server and API endpoints
- **AppMainVerticle**: Main verticle that deploys other verticles
- **Application**: Entry point that starts the Vert.x instance and deploys the AppMainVerticle

## Implementation Details

- The API uses an in-memory repository for data storage
- All operations are asynchronous using Vert.x Futures
- The API follows RESTful conventions for endpoints and HTTP methods
- Proper error handling is implemented for all operations
- The application uses a structured deployment approach:
  - The Application class creates the Vert.x instance and deploys the AppMainVerticle
  - The AppMainVerticle deploys the MainVerticle with multiple instances based on available processors
  - The MainVerticle sets up the HTTP server and API endpoints

## Testing

The project includes comprehensive tests for all CRUD operations. To run the tests:

```
mvn test
```

The tests use Vert.x's WebClient to make HTTP requests to the API endpoints and verify the responses.
