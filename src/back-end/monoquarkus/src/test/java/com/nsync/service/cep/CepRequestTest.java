package com.nsync.service.cep;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test class for {@link CepRequest}.
 * This class contains unit tests for the CepRequest class.
 */
@QuarkusTest
public class CepRequestTest {

    /**
     * Tests the getter and setter for the 'cep' property of {@link CepRequest}.
     */
    @Test
    public void testGetAndSetCep() {
        CepRequest cepRequest = new CepRequest();
        String testCep = "12345-678";
        cepRequest.setCep(testCep);
        assertEquals(testCep, cepRequest.getCep());
    }

    /**
     * Tests the getter and setter for the 'productId' property of {@link CepRequest}.
     */
    @Test
    public void testGetAndSetProductId() {
        CepRequest cepRequest = new CepRequest();
        Long testProductId = 123456789L;
        cepRequest.setProductId(testProductId);
        assertEquals(testProductId, cepRequest.getProductId());
    }
}