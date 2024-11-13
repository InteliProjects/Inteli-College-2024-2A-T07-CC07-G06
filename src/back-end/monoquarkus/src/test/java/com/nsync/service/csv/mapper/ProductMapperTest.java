package com.nsync.service.csv.mapper;

import com.nsync.entity.Product;
import com.nsync.exception.custom.MissingCsvFieldException;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit test for the {@link ProductMapper} class.
 * This class contains several test cases to validate the mapping of CSV records to the Product entity.
 */
@QuarkusTest
public class ProductMapperTest {

    private final ProductMapper productMapper = new ProductMapper();

    /**
     * Tests the mapping of a valid CSV record to a {@link Product} entity.
     * Verifies that the entity is correctly created from a CSV record containing valid fields.
     */
    @Test
    public void testMapToEntity_ValidRecord() {
        Map<String, String> csvRecord = new HashMap<>();
        csvRecord.put("sku", "12345");
        csvRecord.put("name", "Test Product");
        csvRecord.put("price", "19.99");
        csvRecord.put("description", "A test product");
        csvRecord.put("linkImage", "http://example.com/image.jpg");

        Product product = productMapper.mapToEntity(csvRecord);

        assertEquals("12345", product.sku);
        assertEquals("Test Product", product.name);
        assertEquals(19.99, product.price);
        assertEquals("A test product", product.description);
        assertEquals("http://example.com/image.jpg", product.linkImage);
    }

    /**
     * Tests the mapping of a CSV record with missing fields.
     * Verifies that a {@link MissingCsvFieldException} is thrown when a required field is missing.
     */
    @Test
    public void testMapToEntity_MissingFields() {
        Map<String, String> csvRecord = new HashMap<>();
        csvRecord.put("sku", "12345");
        csvRecord.put("name", "Test Product");

        Executable executable = () -> productMapper.mapToEntity(csvRecord);

        MissingCsvFieldException exception = assertThrows(MissingCsvFieldException.class, executable);
        assertTrue(exception.getMessage().contains("Campos faltando no CSV"));
    }

    /**
     * Tests the mapping of a CSV record with an invalid data type.
     * Verifies that a {@link NumberFormatException} is thrown when a field has an invalid data type.
     */
    @Test
    public void testMapToEntity_InvalidDataType() {
        Map<String, String> csvRecord = new HashMap<>();
        csvRecord.put("sku", "12345");
        csvRecord.put("name", "Test Product");
        csvRecord.put("price", "invalid_price");
        csvRecord.put("description", "A test product");
        csvRecord.put("linkImage", "http://example.com/image.jpg");

        Executable executable = () -> productMapper.mapToEntity(csvRecord);

        assertThrows(NumberFormatException.class, executable);
    }

    /**
     * Tests the retrieval of the entity name in {@link ProductMapper}.
     * Verifies that the entity name is correctly returned as "Product".
     */
    @Test
    public void testGetEntityName() {
        assertEquals("Product", productMapper.getEntityName());
    }

    /**
     * Tests the retrieval of the required headers in {@link ProductMapper}.
     * Verifies that the required headers list contains all the mandatory fields.
     */
    @Test
    public void testGetRequiredHeaders() {
        List<String> expectedHeaders = List.of("sku", "name", "price", "description", "linkImage");
        assertEquals(expectedHeaders, productMapper.getRequiredHeaders());
    }

    /**
     * Tests the mapping of an empty CSV record to a {@link Product} entity.
     * Verifies that a {@link MissingCsvFieldException} is thrown when all required fields are missing.
     */
    @Test
    public void testMapToEntity_EmptyRecord() {
        Map<String, String> csvRecord = new HashMap<>();

        Executable executable = () -> productMapper.mapToEntity(csvRecord);

        MissingCsvFieldException exception = assertThrows(MissingCsvFieldException.class, executable);
        assertTrue(exception.getMessage().contains("Campos faltando no CSV"));
    }

    /**
     * Tests the mapping of a CSV record with extra fields to a {@link Product} entity.
     * Verifies that the entity is correctly created from a CSV record containing valid fields,
     * and extra fields are ignored.
     */
    @Test
    public void testMapToEntity_ExtraFields() {
        Map<String, String> csvRecord = new HashMap<>();
        csvRecord.put("sku", "12345");
        csvRecord.put("name", "Test Product");
        csvRecord.put("price", "19.99");
        csvRecord.put("description", "A test product");
        csvRecord.put("linkImage", "http://example.com/image.jpg");
        csvRecord.put("extraField", "extraValue");

        Product product = productMapper.mapToEntity(csvRecord);

        assertEquals("12345", product.sku);
        assertEquals("Test Product", product.name);
        assertEquals(19.99, product.price);
        assertEquals("A test product", product.description);
        assertEquals("http://example.com/image.jpg", product.linkImage);
    }
}
