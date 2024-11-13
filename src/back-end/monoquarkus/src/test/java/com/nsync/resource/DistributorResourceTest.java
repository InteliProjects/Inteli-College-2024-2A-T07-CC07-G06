package com.nsync.resource;

import com.nsync.entity.Distributor;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.greaterThan;

/**
 * Integration test for the {@link DistributorResource} REST API.
 * This class contains several test cases to validate the functionality of the Distributor resource endpoints.
 */
@QuarkusTest
public class DistributorResourceTest {

    /**
     * Tests the creation of a new distributor using the POST endpoint.
     * Verifies that the distributor is created successfully with the correct attributes.
     */
    @Test
    public void testCreateDistributor() {
        // Test to create a new distributor
        Distributor distributor = new Distributor();
        distributor.name = "Test Distributor";
        distributor.cep = "123456789";

        given()
            .contentType(ContentType.JSON)
            .body(distributor)
            .when().post("/distributors")
            .then()
            .statusCode(201)
            .body("name", is("Test Distributor"))
            .body("cep", is("123456789"));
    }

    /**
     * Tests the deletion of an existing distributor using the DELETE endpoint.
     * Verifies that the distributor is deleted successfully and is no longer accessible.
     */
    @Test
    public void testDeleteDistributor() {
        // Assuming distributor with ID 1 exists in the database as per import.sql
        given()
            .when().delete("/distributors/1")
            .then()
            .statusCode(204);

        // Verify that the distributor was actually deleted
        given()
            .when().get("/distributors/1")
            .then()
            .statusCode(404);
    }

    /**
     * Tests listing all distributors using the GET endpoint.
     * Verifies that the list contains at least one distributor.
     */
    @Test
    public void testListDistributors() {
        // Verify that there are distributors in the list
        given()
            .when().get("/distributors")
            .then()
            .statusCode(200)
            .body("size()", greaterThan(0));
    }

    /**
     * Tests retrieving a specific distributor by ID using the GET endpoint.
     * Verifies that the distributor is retrieved successfully with the correct attributes.
     */
    @Test
    public void testGetDistributor() {
        Distributor distributor = new Distributor();
        distributor.name = "Test Distributor";
        distributor.cep = "123456789";

        Response response = given()
            .contentType(ContentType.JSON)
            .body(distributor)
            .when().post("/distributors")
            .then()
            .statusCode(201)
            .extract().response();

        Long distributorId = response.jsonPath().getLong("id");
    
        given()
            .when().get("/distributors/" + distributorId)
            .then()
            .statusCode(200)
            .body("name", is(notNullValue()))
            .body("cep", is(notNullValue()));
    }

    /**
     * Tests updating an existing distributor using the PUT endpoint.
     * Verifies that the distributor is updated successfully with the new attributes.
     */
    @Test
    public void testUpdateDistributor() {
        // Update distributor with ID 1
        Distributor distributor = new Distributor();
        distributor.name = "Updated Distributor";
        distributor.cep = "12345678";

        given()
            .contentType(ContentType.JSON)
            .body(distributor)
            .when().put("/distributors/1")
            .then()
            .statusCode(200)
            .body("name", is("Updated Distributor"))
            .body("cep", is("12345678"));
    }

    /**
     * Tests deleting a non-existent distributor using the DELETE endpoint.
     * Verifies that a 404 status code is returned.
     */
    @Test
    public void testDeleteNonExistentDistributor() {
        // Attempt to delete a distributor that does not exist
        given()
            .when().delete("/distributors/999")
            .then()
            .statusCode(404);
    }
}
