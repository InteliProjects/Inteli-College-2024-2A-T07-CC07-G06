package com.nsync.models;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit test for the {@link ProductRequest} class.
 * This class contains several test cases to validate the functionality and constraints of the ProductRequest model.
 */
@QuarkusTest
public class ProductRequestTest {

    /**
     * Tests the getter and setter for the 'productId' field in {@link ProductRequest}.
     * Verifies that the 'productId' can be set and retrieved correctly.
     */
    @Test
    public void testGetAndSetProductId() {
        ProductRequest productRequest = new ProductRequest();
        Long expectedProductId = 123L;
        productRequest.setProductId(expectedProductId);
        assertEquals(expectedProductId, productRequest.getProductId());
    }

    /**
     * Tests the getter and setter for the 'quantity' field in {@link ProductRequest}.
     * Verifies that the 'quantity' can be set and retrieved correctly.
     */
    @Test
    public void testGetAndSetQuantity() {
        ProductRequest productRequest = new ProductRequest();
        Integer expectedQuantity = 10;
        productRequest.setQuantity(expectedQuantity);
        assertEquals(expectedQuantity, productRequest.getQuantity());
    }

    /**
     * Tests that the 'productId' is not null after being set in {@link ProductRequest}.
     * Verifies that a non-null value is correctly stored and retrieved.
     */
    @Test
    public void testProductIdNotNull() {
        ProductRequest productRequest = new ProductRequest();
        productRequest.setProductId(123L);
        assertNotNull(productRequest.getProductId());
    }

    /**
     * Tests that the 'quantity' is not null after being set in {@link ProductRequest}.
     * Verifies that a non-null value is correctly stored and retrieved.
     */
    @Test
    public void testQuantityNotNull() {
        ProductRequest productRequest = new ProductRequest();
        productRequest.setQuantity(10);
        assertNotNull(productRequest.getQuantity());
    }

    /**
     * Tests setting a null value for 'productId' in {@link ProductRequest}.
     * Verifies that a {@link NullPointerException} is thrown.
     */
    @Test
    public void testSetProductIdWithNull() {
        ProductRequest productRequest = new ProductRequest();
        assertThrows(NullPointerException.class, () -> {
            productRequest.setProductId(null);
        });
    }

    /**
     * Tests setting a null value for 'quantity' in {@link ProductRequest}.
     * Verifies that a {@link NullPointerException} is thrown.
     */
    @Test
    public void testSetQuantityWithNull() {
        ProductRequest productRequest = new ProductRequest();
        assertThrows(NullPointerException.class, () -> {
            productRequest.setQuantity(null);
        });
    }

    /**
     * Tests setting a negative value for 'productId' in {@link ProductRequest}.
     * Verifies that the negative value is correctly stored and retrieved.
     */
    @Test
    public void testSetProductIdWithNegativeValue() {
        ProductRequest productRequest = new ProductRequest();
        Long negativeProductId = -1L;
        productRequest.setProductId(negativeProductId);
        assertEquals(negativeProductId, productRequest.getProductId());
    }

    /**
     * Tests setting a negative value for 'quantity' in {@link ProductRequest}.
     * Verifies that the negative value is correctly stored and retrieved.
     */
    @Test
    public void testSetQuantityWithNegativeValue() {
        ProductRequest productRequest = new ProductRequest();
        Integer negativeQuantity = -5;
        productRequest.setQuantity(negativeQuantity);
        assertEquals(negativeQuantity, productRequest.getQuantity());
    }
}
