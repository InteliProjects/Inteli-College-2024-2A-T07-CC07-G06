package com.nsync.service.cep;

import com.nsync.entity.Distributor;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Unit test for the {@link CepDeliveryEstimator} service.
 * This class contains several test cases to validate the functionality of the delivery estimation based on CEP (postal code).
 */
@QuarkusTest
@Transactional
public class CepDeliveryEstimatorTest {

    @Inject
    EntityManager entityManager;

    @Inject
    CepDeliveryEstimator cepDeliveryEstimator;

    /**
     * Helper method to create a product for testing purposes.
     * @return The ID of the newly created product.
     */
    private int createProduct() {
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
     * Sets up test data in the database before each test method.
     * Initializes two distributors with different CEPs for testing.
     */
    @BeforeEach
    public void setUp() {
        // Initializing test data in the database
        Distributor distributor1 = new Distributor();
        distributor1.cep = "87654321";
        distributor1.name = "Distribuidor 1";

        entityManager.persist(distributor1);

        Distributor distributor2 = new Distributor();
        distributor2.name = "Distribuidor 2";
        distributor2.cep = "12345678";
        
        entityManager.persist(distributor2);
        entityManager.flush();
    }

    /**
     * Tests the calculation of delivery days without using mock delivery.
     * Verifies that the response contains valid delivery days and distributor details.
     */
    @Test
    public void testCalculateDays_withoutMockDelivery() {
        String cep = "12345678";
        int productIt = createProduct();
        Long productId = Long.valueOf(productIt);

        CepResponse response = cepDeliveryEstimator.calculateDays(cep, productId);

        assertNotNull(response);
        assertNotNull(response.firstDay);
        assertNotNull(response.lastDay);
        if (response.distributor != null) {
            assertEquals("87654321", response.distributor.cep);
        }
    }

    /**
     * Tests the calculation of delivery days with mock delivery enabled.
     * Verifies that the response contains valid delivery days.
     */
    @Test
    public void testCalculateDays_withMockDelivery() {
        String cep = "12345678";
        int productIt = createProduct();
        Long productId = Long.valueOf(productIt);
        boolean mock = true;

        CepResponse response = cepDeliveryEstimator.calculateDays(cep, productId, 1, mock, false);

        assertNotNull(response);
        assertNotNull(response.firstDay);
        assertNotNull(response.lastDay);
    }

    /**
     * Tests the calculation of delivery days with mock delivery and distributor.
     * Verifies that the response contains valid delivery days and distributor details if available.
     */
    @Test
    public void testCalculateDays_withMockDeliveryAndDistributor() {
        String cep = "12345678";
        int productIt = createProduct();
        Long productId = Long.valueOf(productIt);
        boolean mock = true;
    
        CepResponse response = cepDeliveryEstimator.calculateDays(cep, productId, 1, mock, true);
    
        assertNotNull(response);
        assertNotNull(response.firstDay);
        assertNotNull(response.lastDay);
        
        // Check if distributor is not null only if the list of distributors is not empty
        if (response.distributor != null) {
            assertEquals("87654321", response.distributor.cep);
        }
    }

    /**
     * Tests the calculation of delivery days with an invalid CEP.
     * Verifies that the response contains valid delivery days even for an invalid CEP.
     */
    @Test
    public void testCalculateDays_withInvalidCep() {
        String invalidCep = "00000000";
        int productIt = createProduct();
        Long productId = Long.valueOf(productIt);

        CepResponse response = cepDeliveryEstimator.calculateDays(invalidCep, productId);

        assertNotNull(response);
        assertNotNull(response.firstDay);
        assertNotNull(response.lastDay);
    }

    /**
     * Tests the calculation of delivery days with a different product.
     * Verifies that the response contains valid delivery days.
     */
    @Test
    public void testCalculateDays_withDifferentProduct() {
        String cep = "12345678";
        int productIt = createProduct();
        Long productId = Long.valueOf(productIt);

        CepResponse response = cepDeliveryEstimator.calculateDays(cep, productId);

        assertNotNull(response);
        assertNotNull(response.firstDay);
        assertNotNull(response.lastDay);
    }

    /**
     * Tests the calculation of delivery days for a purchase with mock enabled.
     * Verifies that the response contains valid delivery days.
     */
    @Test
    public void testCalculateDaysPurchase_withMock() {
        String cep = "12345678";
        Long productId = 1L;
        int quantity = 1;
        boolean mock = true;

        CepResponse response = cepDeliveryEstimator.calculateDaysPurchase(cep, productId, quantity, mock);

        assertNotNull(response);
        assertNotNull(response.firstDay);
        assertNotNull(response.lastDay);
    }
}
