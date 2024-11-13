package com.nsync.service.cep;

import com.nsync.entity.Distributor;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Unit test for the {@link CepPurchaseResponse} class.
 * This class contains several test cases to validate the initialization and behavior of the CepPurchaseResponse model.
 */
@QuarkusTest
public class CepPurchaseResponseTest {

    /**
     * Tests the initialization of a {@link CepPurchaseResponse} object.
     * Verifies that the object is initialized with the correct attributes.
     */
    @Test
    public void testCepPurchaseResponseInitialization() {
        LocalDate firstDay = LocalDate.of(2023, 1, 1);
        LocalDate lastDay = LocalDate.of(2023, 12, 31);
        Long productId = 12345L;
        Distributor distributor = new Distributor(); // Assuming a default constructor exists

        CepPurchaseResponse response = new CepPurchaseResponse(firstDay, lastDay, productId, distributor);

        assertNotNull(response);
        assertEquals(firstDay, response.firstDay);
        assertEquals(lastDay, response.lastDay);
        assertEquals(productId, response.getProductId());
        assertEquals(distributor, response.getDistributor());
    }

    /**
     * Tests the getter for the product ID in {@link CepPurchaseResponse}.
     * Verifies that the correct product ID is returned.
     */
    @Test
    public void testGetProductId() {
        LocalDate firstDay = LocalDate.of(2023, 1, 1);
        LocalDate lastDay = LocalDate.of(2023, 12, 31);
        Long productId = 12345L;
        Distributor distributor = new Distributor();

        CepPurchaseResponse response = new CepPurchaseResponse(firstDay, lastDay, productId, distributor);

        assertEquals(productId, response.getProductId());
    }

    /**
     * Tests the getter for the distributor in {@link CepPurchaseResponse}.
     * Verifies that the correct distributor is returned.
     */
    @Test
    public void testGetDistributor() {
        LocalDate firstDay = LocalDate.of(2023, 1, 1);
        LocalDate lastDay = LocalDate.of(2023, 12, 31);
        Long productId = 12345L;
        Distributor distributor = new Distributor();

        CepPurchaseResponse response = new CepPurchaseResponse(firstDay, lastDay, productId, distributor);

        assertEquals(distributor, response.getDistributor());
    }
}
