package com.nsync.resource;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Integration test for the {@link ProductDistributorResource} REST API.
 * This class contains several test cases to validate the functionality of the Product-Distributor resource endpoints.
 */
@QuarkusTest
public class ProductDistributorResourceTest {

    /**
     * Helper method to create a product with all required fields.
     * @return The ID of the newly created product.
     */
    private int createProduct() {
        // Creates a product with all required fields
        return given()
            .contentType("application/json")
            .body("{ \"name\": \"Product1\", \"description\": \"Sample description\", \"sku\": \"SKU12345\", \"price\": 100.0, \"linkImage\": \"http://example.com/image.png\" }")
        .when()
            .post("/products")
        .then()
            .statusCode(201)
            .extract()
            .path("id");
    }

    /**
     * Helper method to create a distributor.
     * @return The ID of the newly created distributor.
     */
    private int createDistributor() {
        // Creates a distributor and returns the generated ID
        return given()
            .contentType("application/json")
            .body("{ \"name\": \"Distributor1\", \"cep\": \"123454545\" }") // Add necessary attributes to create the distributor
        .when()
            .post("/distributors")
        .then()
            .statusCode(201)
            .extract()
            .path("id");
    }

    /**
     * Helper method to create a ProductDistributor.
     * @param productId The ID of the product.
     * @param distributorId The ID of the distributor.
     * @return The ID of the newly created ProductDistributor.
     */
    private int createProductDistributor(int productId, int distributorId) {
        return given()
            .contentType("application/json")
            .body("{ \"product\": { \"id\": " + productId + " }, \"distributor\": { \"id\": " + distributorId + " }, \"quantityAvailable\": 100, \"quantityReserved\": 50 }")
        .when()
            .post("/products-distributors")
        .then()
            .statusCode(201)
            .extract()
            .path("id");
    }

    /**
     * Tests the creation of a new ProductDistributor using the POST endpoint.
     * Verifies that the ProductDistributor is created successfully with the correct attributes.
     */
    @Test
    public void testCreateProductDistributor() {
        int productId = createProduct();
        int distributorId = createDistributor();

        int productDistributorId = createProductDistributor(productId, distributorId);

        assertNotNull(productDistributorId);
    }

    /**
     * Tests listing all ProductDistributors using the GET endpoint.
     * Verifies that the list contains at least one ProductDistributor.
     */
    @Test
    public void testGetAllProductDistributors() {
        int productId = createProduct();
        int distributorId = createDistributor();

        createProductDistributor(productId, distributorId);

        given()
            .when()
            .get("/products-distributors")
        .then()
            .statusCode(200)
            .body("size()", greaterThan(0));
    }

    /**
     * Tests retrieving a specific ProductDistributor by ID using the GET endpoint.
     * Verifies that the ProductDistributor is retrieved successfully with the correct attributes.
     */
    @Test
    public void testGetProductDistributor() {
        int productId = createProduct();
        int distributorId = createDistributor();

        int productDistributorId = createProductDistributor(productId, distributorId);

        given()
            .when()
            .get("/products-distributors/" + productDistributorId)
        .then()
            .statusCode(200)
            .body("id", equalTo(productDistributorId))
            .body("product.id", equalTo(productId))
            .body("distributor.id", equalTo(distributorId));
    }

    /**
     * Tests updating an existing ProductDistributor using the PUT endpoint.
     * Verifies that the ProductDistributor is updated successfully with the new attributes.
     */
    @Test
    public void testUpdateProductDistributor() {
        int productId = createProduct();
        int distributorId = createDistributor();

        int productDistributorId = given()
            .contentType("application/json")
            .body("{ \"product\": { \"id\": " + productId + " }, \"distributor\": { \"id\": " + distributorId + " }, \"quantityAvailable\": 100, \"quantityReserved\": 50 }")
        .when()
            .post("/products-distributors")
        .then()
            .statusCode(201)
            .extract()
            .path("id");

        given()
            .contentType("application/json")
            .body("{ \"product\": { \"id\": " + productId + " }, \"distributor\": { \"id\": " + distributorId + " }, \"quantityAvailable\": 200, \"quantityReserved\": 100 }")
        .when()
            .put("/products-distributors/" + productDistributorId)
        .then()
            .statusCode(200)
            .body("quantityAvailable", equalTo(200))
            .body("quantityReserved", equalTo(100));
    }

    /**
     * Tests deleting an existing ProductDistributor using the DELETE endpoint.
     * Verifies that the ProductDistributor is deleted successfully and is no longer accessible.
     */
    @Test
    public void testDeleteProductDistributor() {
        int productId = createProduct();
        int distributorId = createDistributor();

        int productDistributorId = given()
            .contentType("application/json")
            .body("{ \"product\": { \"id\": " + productId + " }, \"distributor\": { \"id\": " + distributorId + " }, \"quantityAvailable\": 100, \"quantityReserved\": 50 }")
        .when()
            .post("/products-distributors")
        .then()
            .statusCode(201)
            .extract()
            .path("id");

        given()
            .when()
            .delete("/products-distributors/" + productDistributorId)
        .then()
            .statusCode(204);

        given()
            .when()
            .get("/products-distributors/" + productDistributorId)
        .then()
            .statusCode(404);
    }
}
