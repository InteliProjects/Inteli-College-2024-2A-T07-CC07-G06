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
 * Integration test for the {@link SalesOrdersResource} REST API.
 * This class contains several test cases to validate the functionality of the Sales Orders resource endpoints.
 */
@QuarkusTest
public class SalesOrdersResourceTest {

    /**
     * Helper method to create a distributor for associating with a sales order.
     * @return The ID of the newly created distributor.
     */
    private int createDistributor() {
        // Create a distributor to associate with the sales order
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
     * Helper method to create a Sales Order associated with a distributor.
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
     * Tests the creation of a new sales order using the POST endpoint.
     * Verifies that the sales order is created successfully with a valid ID.
     */
    @Test
    public void testCreateSalesOrder() {
        int distributorId = createDistributor();
        int salesOrderId = createSalesOrder(distributorId);
        assertNotNull(salesOrderId);
    }

    /**
     * Tests retrieving a specific sales order by ID using the GET endpoint.
     * Verifies that the sales order is retrieved successfully with the correct attributes.
     */
    @Test
    public void testGetSalesOrder() {
        int distributorId = createDistributor();
        int salesOrderId = createSalesOrder(distributorId);

        given()
            .when()
            .get("/sales-orders/" + salesOrderId)
        .then()
            .statusCode(200)
            .body("id", equalTo(salesOrderId))
            .body("status", equalTo("PENDING"));
    }

    /**
     * Tests listing all sales orders using the GET endpoint.
     * Verifies that the list contains at least one sales order.
     */
    @Test
    public void testGetAllSalesOrders() {
       int distributorId = createDistributor();
       createSalesOrder(distributorId);

       given()
            .when()
            .get("/sales-orders")
       .then()
            .statusCode(200)
            .body("size()", greaterThan(0));
    }

    /**
     * Tests updating an existing sales order using the PUT endpoint.
     * Verifies that the sales order is updated successfully with the new attributes.
     */
    @Test
    public void testUpdateSalesOrder() {
        int distributorId = createDistributor();
        int salesOrderId = createSalesOrder(distributorId);

        // Update sales order details
        String saleDate = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        String firstDeliveryDate = LocalDate.now().plusDays(1).format(DateTimeFormatter.ISO_LOCAL_DATE);
        String lastDeliveryDate = LocalDate.now().plusDays(3).format(DateTimeFormatter.ISO_LOCAL_DATE);

        given()
            .contentType("application/json")
            .body("{ \"distributor\": { \"id\": " + distributorId + " }, \"customerCep\": \"87654321\", " +
                  "\"saleDate\": \"" + saleDate + "\", \"total\": 200.0, \"status\": \"COMPLETED\", " +
                  "\"firstDeliveryDate\": \"" + firstDeliveryDate + "\", \"lastDeliveryDate\": \"" + lastDeliveryDate + "\" }")
        .when()
            .put("/sales-orders/" + salesOrderId)
        .then()
            .statusCode(200)
            .body("total", equalTo(200.0f))
            .body("status", equalTo("COMPLETED"));
    }

    /**
     * Tests deleting an existing sales order using the DELETE endpoint.
     * Verifies that the sales order is deleted successfully and is no longer accessible.
     */
    @Test
    public void testDeleteSalesOrder() {
        int distributorId = createDistributor();
        int salesOrderId = createSalesOrder(distributorId);

        // Delete the sales order
        given()
            .when()
            .delete("/sales-orders/" + salesOrderId)
        .then()
            .statusCode(204);

        // Verify that the sales order was actually deleted
        given()
            .when()
            .get("/sales-orders/" + salesOrderId)
        .then()
            .statusCode(404);
    }

    /**
     * Tests updating a non-existent sales order using the PUT endpoint.
     * Verifies that a 404 status code is returned.
     */
    @Test
    public void testUpdateNonExistentSalesOrder() {
        int nonExistentSalesOrderId = 9999;

        given()
            .contentType("application/json")
            .body("{ \"distributor\": { \"id\": 1 }, \"customerCep\": \"87654321\", " +
                  "\"saleDate\": \"" + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + "\", " +
                  "\"total\": 200.0, \"status\": \"COMPLETED\", " +
                  "\"firstDeliveryDate\": \"" + LocalDate.now().plusDays(1).format(DateTimeFormatter.ISO_LOCAL_DATE) + "\", " +
                  "\"lastDeliveryDate\": \"" + LocalDate.now().plusDays(3).format(DateTimeFormatter.ISO_LOCAL_DATE) + "\" }")
        .when()
            .put("/sales-orders/" + nonExistentSalesOrderId)
        .then()
            .statusCode(404);
    }

    /**
     * Tests deleting a non-existent sales order using the DELETE endpoint.
     * Verifies that a 404 status code is returned.
     */
    @Test
    public void testDeleteNonExistentSalesOrder() {
        int nonExistentSalesOrderId = 9999;

        given()
            .when()
            .delete("/sales-orders/" + nonExistentSalesOrderId)
        .then()
            .statusCode(404);
    }
}
