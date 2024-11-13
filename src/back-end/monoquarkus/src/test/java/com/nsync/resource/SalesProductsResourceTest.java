package com.nsync.resource;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Integration test for the {@link SalesProductsResource} REST API.
 * This class contains several test cases to validate the functionality of the Sales Products resource endpoints.
 */
@QuarkusTest
public class SalesProductsResourceTest {

    /**
     * Helper method to create a distributor.
     * @return The ID of the newly created distributor.
     */
    private int createDistributor() {
        return given()
            .contentType("application/json")
            .body("{ \"name\": \"Distributor1\", \"cep\": \"123454545\" }")
        .when()
            .post("/distributors")
        .then()
            .statusCode(201)
            .extract()
            .path("id");
    }

    /**
     * Helper method to create a product.
     * @return The ID of the newly created product.
     */
    private int createProduct() {
        return given()
            .contentType("application/json")
            .body("{ \"name\": \"Product1\", \"price\": 50.0, \"description\": \"Product description\", " +
                  "\"linkImage\": \"http://example.com/product-image.jpg\", \"sku\": \"SKU123\" }")  
        .when()
            .post("/products")
        .then()
            .statusCode(201)
            .extract()
            .path("id");
    }

    /**
     * Helper method to create a sales order associated with a distributor.
     * @param distributorId The ID of the distributor.
     * @return The ID of the newly created sales order.
     */
    private int createSalesOrder(int distributorId) {
        String saleDate = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        String firstDeliveryDate = LocalDate.now().plusDays(1).format(DateTimeFormatter.ISO_LOCAL_DATE);
        String lastDeliveryDate = LocalDate.now().plusDays(3).format(DateTimeFormatter.ISO_LOCAL_DATE);
        
        return given()
            .contentType("application/json")
            .body("{ \"distributor\": { \"id\": " + distributorId + " }, \"customerCep\": \"12345678\", " +
                  "\"saleDate\": \"" + saleDate + "\", \"total\": 150.0, \"status\": \"PENDING\", " +  
                  "\"firstDeliveryDate\": \"" + firstDeliveryDate + "\", \"lastDeliveryDate\": \"" + lastDeliveryDate + "\" }")
        .when()
            .post("/sales-orders")
        .then()
            .statusCode(201)  
            .extract()
            .path("id");
    }

    /**
     * Helper method to create a sales product associated with an order, product, and distributor.
     * @param orderId The ID of the sales order.
     * @param productId The ID of the product.
     * @param distributorId The ID of the distributor.
     * @return The ID of the newly created sales product.
     */
    private int createSalesProduct(int orderId, int productId, int distributorId) {
        return given()
            .contentType("application/json")
            .body("{ \"order\": { \"id\": " + orderId + " }, \"product\": { \"id\": " + productId + " }, " +
                  "\"distributor\": { \"id\": " + distributorId + " }, \"quantity\": 2, \"unitPrice\": 25.0 }")
        .when()
            .post("/sales-products")
        .then()
            .statusCode(201)
            .extract()
            .path("id");
    }

    /**
     * Tests the creation of a new sales product using the POST endpoint.
     * Verifies that the sales product is created successfully with a valid ID.
     */
    @Test
    public void testCreateSalesProduct() {
        int distributorId = createDistributor();
        int productId = createProduct();
        int orderId = createSalesOrder(distributorId);
        int salesProductId = createSalesProduct(orderId, productId, distributorId);
        assertNotNull(salesProductId);
    }

    /**
     * Tests retrieving a specific sales product by ID using the GET endpoint.
     * Verifies that the sales product is retrieved successfully with the correct attributes.
     */
    @Test
    public void testGetSalesProduct() {
        int distributorId = createDistributor();
        int productId = createProduct();
        int orderId = createSalesOrder(distributorId);
        int salesProductId = createSalesProduct(orderId, productId, distributorId);

        given()
            .when()
            .get("/sales-products/" + salesProductId)
        .then()
            .statusCode(200)
            .body("id", equalTo(salesProductId))
            .body("order.id", equalTo(orderId))
            .body("product.id", equalTo(productId))
            .body("distributor.id", equalTo(distributorId))
            .body("quantity", equalTo(2))
            .body("unitPrice", equalTo(25.0f));
    }

    /**
     * Tests listing all sales products using the GET endpoint.
     * Verifies that the list contains at least one sales product.
     */
    @Test
    public void testGetAllSalesOrders() {
        int distributorId = createDistributor();
        int orderId = createSalesOrder(distributorId);
        createSalesProduct(orderId, createProduct(), distributorId);

        given()
            .when()
            .get("/sales-products")
        .then()
            .statusCode(200)
            .body("size()", greaterThan(0));
    }

    /**
     * Tests updating an existing sales product using the PUT endpoint.
     * Verifies that the sales product is updated successfully with the new attributes.
     */
    @Test
    public void testUpdateSalesProduct() {
        int distributorId = createDistributor();
        int productId = createProduct();
        int orderId = createSalesOrder(distributorId);
        int salesProductId = createSalesProduct(orderId, productId, distributorId);

        given()
            .contentType("application/json")
            .body("{ \"order\": { \"id\": " + orderId + " }, \"product\": { \"id\": " + productId + " }, " +
                  "\"distributor\": { \"id\": " + distributorId + " }, \"quantity\": 3, \"unitPrice\": 30.0 }")
        .when()
            .put("/sales-products/" + salesProductId)
        .then()
            .statusCode(200)
            .body("quantity", equalTo(3))
            .body("unitPrice", equalTo(30.0f));
    }

    /**
     * Tests deleting an existing sales product using the DELETE endpoint.
     * Verifies that the sales product is deleted successfully and is no longer accessible.
     */
    @Test
    public void testDeleteSalesProduct() {
        int distributorId = createDistributor();
        int productId = createProduct();
        int orderId = createSalesOrder(distributorId);
        int salesProductId = createSalesProduct(orderId, productId, distributorId);

        // Delete the sales product
        given()
            .when()
            .delete("/sales-products/" + salesProductId)
        .then()
            .statusCode(204);

        // Verify that the sales product was indeed deleted
        given()
            .when()
            .get("/sales-products/" + salesProductId)
        .then()
            .statusCode(404);
    }

    /**
     * Tests retrieving a non-existent sales product using the GET endpoint.
     * Verifies that a 404 status code is returned.
     */
    @Test
    public void testGetNonExistentSalesProduct() {
        given()
            .when()
            .get("/sales-products/999999")
        .then()
            .statusCode(404);
    }
}
