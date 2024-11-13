package com.nsync.service.csv;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * Unit test for the {@link MultipleFileUploadForm} class.
 * This class contains several test cases to validate the behavior of handling multiple file uploads.
 */
@QuarkusTest
public class MultipleFileUploadFormTest {

    private MultipleFileUploadForm form;
    private InputStream mockFileDistributor;
    private InputStream mockFileProduct;
    private InputStream mockFileProductDistributor;
    private InputStream mockFileSalesOrders;
    private InputStream mockFileSalesProducts;

    /**
     * Sets up the test environment by initializing the {@link MultipleFileUploadForm} object and mocking input streams for the tests.
     */
    @BeforeEach
    public void setUp() {
        form = new MultipleFileUploadForm();
        mockFileDistributor = mock(InputStream.class);
        mockFileProduct = mock(InputStream.class);
        mockFileProductDistributor = mock(InputStream.class);
        mockFileSalesOrders = mock(InputStream.class);
        mockFileSalesProducts = mock(InputStream.class);
    }

    /**
     * Tests the setter and getter for the distributor file in {@link MultipleFileUploadForm}.
     * Verifies that the distributor file can be set and retrieved correctly.
     */
    @Test
    public void testSetAndGetFileDistributor() {
        form.setFileDistributor(mockFileDistributor);
        assertEquals(mockFileDistributor, form.getFileDistributor());
    }

    /**
     * Tests the setter and getter for the product file in {@link MultipleFileUploadForm}.
     * Verifies that the product file can be set and retrieved correctly.
     */
    @Test
    public void testSetAndGetFileProduct() {
        form.setFileProduct(mockFileProduct);
        assertEquals(mockFileProduct, form.getFileProduct());
    }

    /**
     * Tests the setter and getter for the product distributor file in {@link MultipleFileUploadForm}.
     * Verifies that the product distributor file can be set and retrieved correctly.
     */
    @Test
    public void testSetAndGetFileProductDistributor() {
        form.setFileProductDistributor(mockFileProductDistributor);
        assertEquals(mockFileProductDistributor, form.getFileProductDistributor());
    }

    /**
     * Tests the setter and getter for the sales orders file in {@link MultipleFileUploadForm}.
     * Verifies that the sales orders file can be set and retrieved correctly.
     */
    @Test
    public void testSetAndGetFileSalesOrders() {
        form.setFileSalesOrders(mockFileSalesOrders);
        assertEquals(mockFileSalesOrders, form.getFileSalesOrders());
    }

    /**
     * Tests the setter and getter for the sales products file in {@link MultipleFileUploadForm}.
     * Verifies that the sales products file can be set and retrieved correctly.
     */
    @Test
    public void testSetAndGetFileSalesProducts() {
        form.setFileSalesProducts(mockFileSalesProducts);
        assertEquals(mockFileSalesProducts, form.getFileSalesProducts());
    }
}
