package com.nsync.service.cep;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;

/**
 * Test class for CepResource.
 * This class contains tests for the CepResource REST endpoints.
 */
@QuarkusTest
public class CepResourceTest {

    @Inject
    CepDeliveryEstimator cepDeliveryEstimator;

    /**
     * Tests the /cep/calculate-days endpoint with a valid CEP.
     * Verifies that the response contains the expected fields.
     */
    @Test
    public void testCalculateDaysValidCep() {
        CepRequest validRequest = new CepRequest();
        validRequest.setCep("12345-678");

        given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(validRequest)
        .when()
            .post("/cep/calculate-days")
        .then()
            .statusCode(200)
            .body(containsString("firstDay"))
            .body(containsString("lastDay"));
    }


    /**
     * Tests the /cep/calculate-days endpoint with an invalid CEP.
     * Verifies that the response contains an error message.
     */
    @Test
    public void testCalculateDaysInvalidCep() {
        CepRequest invalidRequest = new CepRequest();
        invalidRequest.setCep("invalid-cep");

        given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(invalidRequest)
        .when()
            .post("/cep/calculate-days")
        .then()
            .statusCode(400)
            .body(containsString("Invalid CEP format. Expected format: 12345-678 or 12345678."));
    }

    /**
     * Tests the /cep/calculate-days endpoint with a missing CEP.
     * Verifies that the response contains an error message.
     */
    @Test
    public void testCalculateDaysMissingCep() {
        CepRequest missingCepRequest = new CepRequest();
        missingCepRequest.setProductId(12345L);

        given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(missingCepRequest)
        .when()
            .post("/cep/calculate-days")
        .then()
            .statusCode(400)
            .body(containsString("Invalid CEP format. Expected format: 12345-678 or 12345678."));
    }

    /**
     * Tests the /cep/calculate-days endpoint with a valid product ID.
     * Verifies that the response contains the expected fields.
     */
    @Test
    public void testCalculateDaysMissingProductId() {
        CepRequest missingProductIdRequest = new CepRequest();
        missingProductIdRequest.setCep("12345-678");

        given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(missingProductIdRequest)
        .when()
            .post("/cep/calculate-days")
        .then()
            .statusCode(200)
            .body(containsString("firstDay"))
            .body(containsString("lastDay"));
    }
}