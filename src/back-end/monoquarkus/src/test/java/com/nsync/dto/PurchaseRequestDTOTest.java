package com.nsync.dto;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Unit test for the {@link PurchaseRequestDTO} class.
 * This class contains several test cases to validate the functionality of the PurchaseRequestDTO class.
 */
@QuarkusTest
public class PurchaseRequestDTOTest {

    /**
     * Tests the getter and setter for the 'products' field in {@link PurchaseRequestDTO}.
     * Verifies that the 'products' list can be set and retrieved correctly.
     */
    @Test
    public void testGetAndSetProducts() {
        PurchaseRequestDTO purchaseRequestDTO = new PurchaseRequestDTO();
        ProductRequestDTO product1 = new ProductRequestDTO();
        ProductRequestDTO product2 = new ProductRequestDTO();
        List<ProductRequestDTO> products = Arrays.asList(product1, product2);

        purchaseRequestDTO.setProducts(products);
        List<ProductRequestDTO> retrievedProducts = purchaseRequestDTO.getProducts();

        assertNotNull(retrievedProducts);
        assertEquals(2, retrievedProducts.size());
        assertEquals(product1, retrievedProducts.get(0));
        assertEquals(product2, retrievedProducts.get(1));
    }

    /**
     * Tests the getter and setter for the 'cep' field in {@link PurchaseRequestDTO}.
     * Verifies that the 'cep' value can be set and retrieved correctly.
     */
    @Test
    public void testGetAndSetCep() {
        PurchaseRequestDTO purchaseRequestDTO = new PurchaseRequestDTO();
        String cep = "12345-678";

        purchaseRequestDTO.setCep(cep);
        String retrievedCep = purchaseRequestDTO.getCep();

        assertNotNull(retrievedCep);
        assertEquals(cep, retrievedCep);
    }

    /**
     * Tests the {@link PurchaseRequestDTO#toString()} method.
     * Verifies that the string representation of the object is not null.
     */
    @Test
    public void testToString() {
        PurchaseRequestDTO purchaseRequestDTO = new PurchaseRequestDTO();
        assertNotNull(purchaseRequestDTO.toString());
    }

    /**
     * Tests the default constructor of {@link PurchaseRequestDTO}.
     * Verifies that a new instance is not null and that its fields have default null values.
     */
    @Test
    public void testEmptyConstructor() {
        PurchaseRequestDTO purchaseRequestDTO = new PurchaseRequestDTO();
        assertNotNull(purchaseRequestDTO);
        assertNull(purchaseRequestDTO.getProducts());
        assertNull(purchaseRequestDTO.getCep());
    }

    /**
     * Tests setting the 'products' field to null in {@link PurchaseRequestDTO}.
     * Verifies that the 'products' list is set to null correctly.
     */
    @Test
    public void testSetProductsToNull() {
        PurchaseRequestDTO purchaseRequestDTO = new PurchaseRequestDTO();
        purchaseRequestDTO.setProducts(null);
        assertNull(purchaseRequestDTO.getProducts());
    }

    /**
     * Tests setting the 'cep' field to null in {@link PurchaseRequestDTO}.
     * Verifies that the 'cep' value is set to null correctly.
     */
    @Test
    public void testSetCepToNull() {
        PurchaseRequestDTO purchaseRequestDTO = new PurchaseRequestDTO();
        purchaseRequestDTO.setCep(null);
        assertNull(purchaseRequestDTO.getCep());
    }

    /**
     * Tests setting an empty list for the 'products' field in {@link PurchaseRequestDTO}.
     * Verifies that the 'products' list is set and retrieved correctly as an empty list.
     */
    @Test
    public void testSetEmptyProductsList() {
        PurchaseRequestDTO purchaseRequestDTO = new PurchaseRequestDTO();
        List<ProductRequestDTO> emptyProducts = Arrays.asList();
        purchaseRequestDTO.setProducts(emptyProducts);
        List<ProductRequestDTO> retrievedProducts = purchaseRequestDTO.getProducts();

        assertNotNull(retrievedProducts);
        assertEquals(0, retrievedProducts.size());
    }

    /**
     * Tests setting an empty string for the 'cep' field in {@link PurchaseRequestDTO}.
     * Verifies that the 'cep' value can be set and retrieved correctly as an empty string.
     */
    @Test
    public void testSetEmptyCep() {
        PurchaseRequestDTO purchaseRequestDTO = new PurchaseRequestDTO();
        String emptyCep = "";

        purchaseRequestDTO.setCep(emptyCep);
        String retrievedCep = purchaseRequestDTO.getCep();

        assertNotNull(retrievedCep);
        assertEquals(emptyCep, retrievedCep);
    }
}
