package com.nsync.dto;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Unit test for the {@link ProductRequestDTO} class.
 * This class contains several test cases to validate the functionality of the ProductRequestDTO class.
 */
@QuarkusTest
public class ProductRequestDTOTest {

    /**
     * Tests the initialization of a {@link ProductRequestDTO} object.
     * Verifies that the object is not null after instantiation.
     */
    @Test
    public void testInitialization() {
        ProductRequestDTO productRequestDTO = new ProductRequestDTO();
        assertNotNull(productRequestDTO);
    }

    /**
     * Tests the getter and setter for the 'productId' field in {@link ProductRequestDTO}.
     * Verifies that the 'productId' can be set and retrieved correctly.
     */
    @Test
    public void testGetAndSetProductId() {
        ProductRequestDTO productRequestDTO = new ProductRequestDTO();
        Long productId = 123L;
        productRequestDTO.setProductId(productId);
        assertEquals(productId, productRequestDTO.getProductId());
    }

    /**
     * Tests the getter and setter for the 'quantity' field in {@link ProductRequestDTO}.
     * Verifies that the 'quantity' can be set and retrieved correctly.
     */
    @Test
    public void testGetAndSetQuantity() {
        ProductRequestDTO productRequestDTO = new ProductRequestDTO();
        Integer quantity = 10;
        productRequestDTO.setQuantity(quantity);
        assertEquals(quantity, productRequestDTO.getQuantity());
    }

    /**
     * Tests the {@link ProductRequestDTO#toString()} method.
     * Verifies that the string representation of the object is not null.
     */
    @Test
    public void testToString() {
        ProductRequestDTO productRequestDTO = new ProductRequestDTO();
        assertNotNull(productRequestDTO.toString());
    }

    /**
     * Tests the inequality of two different {@link ProductRequestDTO} objects.
     * Verifies that two objects with different values for 'productId' and 'quantity' are not equal.
     */
    @Test
    public void testNotEquals() {
        ProductRequestDTO productRequestDTO1 = new ProductRequestDTO();
        productRequestDTO1.setProductId(123L);
        productRequestDTO1.setQuantity(10);

        ProductRequestDTO productRequestDTO2 = new ProductRequestDTO();
        productRequestDTO2.setProductId(124L);
        productRequestDTO2.setQuantity(11);

        assertNotEquals(productRequestDTO1, productRequestDTO2);
    }

    /**
     * Tests the default values of a newly instantiated {@link ProductRequestDTO} object.
     * Verifies that the 'productId' and 'quantity' fields are null by default.
     */
    @Test
    public void testDefaultValues() {
        ProductRequestDTO productRequestDTO = new ProductRequestDTO();
        assertNull(productRequestDTO.getProductId());
        assertNull(productRequestDTO.getQuantity());
    }
}
