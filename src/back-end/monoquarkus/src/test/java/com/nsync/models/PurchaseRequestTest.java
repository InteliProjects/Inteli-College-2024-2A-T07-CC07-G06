package com.nsync.models;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Unit test for the {@link PurchaseRequest} class.
 * This class contains several test cases to validate the functionality and behavior of the PurchaseRequest model.
 */
@QuarkusTest
public class PurchaseRequestTest {

    private PurchaseRequest purchaseRequest;
    private List<ProductRequest> productList;
    private String cep;

    /**
     * Initializes test data before each test method.
     */
    @BeforeEach
    public void setUp() {
        purchaseRequest = new PurchaseRequest();
        productList = Arrays.asList(new ProductRequest(), new ProductRequest());
        cep = "12345-678";
    }

    /**
     * Tests the getter for the 'products' field in {@link PurchaseRequest}.
     * Verifies that the list of products is set and retrieved correctly.
     */
    @Test
    public void testGetProducts() {
        purchaseRequest.setProducts(productList);
        List<ProductRequest> result = purchaseRequest.getProducts();
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(productList, result);
    }

    /**
     * Tests the setter for the 'products' field in {@link PurchaseRequest}.
     * Verifies that the list of products is correctly stored.
     */
    @Test
    public void testSetProducts() {
        purchaseRequest.setProducts(productList);
        assertEquals(productList, purchaseRequest.getProducts());
    }

    /**
     * Tests the getter for the 'cep' field in {@link PurchaseRequest}.
     * Verifies that the 'cep' value is set and retrieved correctly.
     */
    @Test
    public void testGetCep() {
        purchaseRequest.setCep(cep);
        String result = purchaseRequest.getCep();
        assertNotNull(result);
        assertEquals(cep, result);
    }

    /**
     * Tests the setter for the 'cep' field in {@link PurchaseRequest}.
     * Verifies that the 'cep' value is correctly stored.
     */
    @Test
    public void testSetCep() {
        purchaseRequest.setCep(cep);
        assertEquals(cep, purchaseRequest.getCep());
    }

    /**
     * Tests setting the 'products' field to null in {@link PurchaseRequest}.
     * Verifies that the 'products' list is set to null correctly.
     */
    @Test
    public void testSetProductsToNull() {
        purchaseRequest.setProducts(null);
        assertNull(purchaseRequest.getProducts());
    }

    /**
     * Tests setting the 'cep' field to null in {@link PurchaseRequest}.
     * Verifies that the 'cep' value is set to null correctly.
     */
    @Test
    public void testSetCepToNull() {
        purchaseRequest.setCep(null);
        assertNull(purchaseRequest.getCep());
    }

    /**
     * Tests setting an empty list for the 'products' field in {@link PurchaseRequest}.
     * Verifies that the 'products' list is set and retrieved correctly as an empty list.
     */
    @Test
    public void testEmptyProductList() {
        List<ProductRequest> emptyList = Arrays.asList();
        purchaseRequest.setProducts(emptyList);
        List<ProductRequest> result = purchaseRequest.getProducts();
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    /**
     * Tests setting an empty string for the 'cep' field in {@link PurchaseRequest}.
     * Verifies that the 'cep' value can be set and retrieved correctly as an empty string.
     */
    @Test
    public void testSetCepWithEmptyString() {
        purchaseRequest.setCep("");
        String result = purchaseRequest.getCep();
        assertNotNull(result);
        assertEquals("", result);
    }

    /**
     * Tests setting a whitespace string for the 'cep' field in {@link PurchaseRequest}.
     * Verifies that the 'cep' value can be set and retrieved correctly as a whitespace string.
     */
    @Test
    public void testSetCepWithWhitespace() {
        purchaseRequest.setCep("   ");
        String result = purchaseRequest.getCep();
        assertNotNull(result);
        assertEquals("   ", result);
    }
}
