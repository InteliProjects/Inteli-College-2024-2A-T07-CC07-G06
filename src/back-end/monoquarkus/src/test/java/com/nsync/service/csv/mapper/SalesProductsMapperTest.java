package com.nsync.service.csv.mapper;

import com.nsync.entity.SalesProducts;
import com.nsync.exception.custom.MissingCsvFieldException;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit test for the {@link SalesProductsMapper} class.
 * This class contains several test cases to validate the mapping of CSV records to the SalesProducts entity.
 */
@QuarkusTest
public class SalesProductsMapperTest {

    /**
     * Tests the mapping of a valid CSV record to a {@link SalesProducts} entity.
     * Verifies that the entity is correctly created from a CSV record containing valid fields.
     */
    @Test
    public void testMapToEntity_ValidRecord() {
        SalesProductsMapper mapper = new SalesProductsMapper();
        Map<String, String> csvRecord = new HashMap<>();
        csvRecord.put("sales_orders_id", "1");
        csvRecord.put("product_id", "2");
        csvRecord.put("distributors_id", "3");
        csvRecord.put("quantity", "10");
        csvRecord.put("unitPrice", "99.99");

        SalesProducts salesProducts = mapper.mapToEntity(csvRecord);

        assertNotNull(salesProducts);
        assertEquals(1L, salesProducts.order.id);
        assertEquals(2L, salesProducts.product.id);
        assertEquals(3L, salesProducts.distributor.id);
        assertEquals(10, salesProducts.quantity);
        assertEquals(99.99, salesProducts.unitPrice);
    }

    /**
     * Tests the mapping of a CSV record with missing headers.
     * Verifies that a {@link MissingCsvFieldException} is thrown when a required field is missing.
     */
    @Test
    public void testMapToEntity_MissingHeaders() {
        SalesProductsMapper mapper = new SalesProductsMapper();
        Map<String, String> csvRecord = new HashMap<>();
        csvRecord.put("sales_orders_id", "1");
        csvRecord.put("product_id", "2");

        Executable executable = () -> mapper.mapToEntity(csvRecord);

        MissingCsvFieldException exception = assertThrows(MissingCsvFieldException.class, executable);
        assertTrue(exception.getMessage().contains("Campos faltando no CSV: distributors_id, quantity, unitPrice"));
    }

    /**
     * Tests the mapping of a CSV record with an invalid quantity.
     * Verifies that a {@link NumberFormatException} is thrown when the quantity field has an invalid data type.
     */
    @Test
    public void testMapToEntity_InvalidQuantity() {
        SalesProductsMapper mapper = new SalesProductsMapper();
        Map<String, String> csvRecord = new HashMap<>();
        csvRecord.put("sales_orders_id", "1");
        csvRecord.put("product_id", "2");
        csvRecord.put("distributors_id", "3");
        csvRecord.put("quantity", "invalid");
        csvRecord.put("unitPrice", "99.99");

        Executable executable = () -> mapper.mapToEntity(csvRecord);

        NumberFormatException exception = assertThrows(NumberFormatException.class, executable);
        assertTrue(exception.getMessage().contains("For input string: \"invalid\""));
    }

    /**
     * Tests the mapping of a CSV record with an invalid unit price.
     * Verifies that a {@link NumberFormatException} is thrown when the unitPrice field has an invalid data type.
     */
    @Test
    public void testMapToEntity_InvalidUnitPrice() {
        SalesProductsMapper mapper = new SalesProductsMapper();
        Map<String, String> csvRecord = new HashMap<>();
        csvRecord.put("sales_orders_id", "1");
        csvRecord.put("product_id", "2");
        csvRecord.put("distributors_id", "3");
        csvRecord.put("quantity", "10");
        csvRecord.put("unitPrice", "invalid");

        Executable executable = () -> mapper.mapToEntity(csvRecord);

        NumberFormatException exception = assertThrows(NumberFormatException.class, executable);
        assertTrue(exception.getMessage().contains("For input string: \"invalid\""));
    }

    /**
     * Tests the retrieval of the entity name in {@link SalesProductsMapper}.
     * Verifies that the entity name is correctly returned as "SalesProducts".
     */
    @Test
    public void testGetEntityName() {
        SalesProductsMapper mapper = new SalesProductsMapper();
        assertEquals("SalesProducts", mapper.getEntityName());
    }

    /**
     * Tests the retrieval of the required headers in {@link SalesProductsMapper}.
     * Verifies that the required headers list contains all the mandatory fields.
     */
    @Test
    public void testGetRequiredHeaders() {
        SalesProductsMapper mapper = new SalesProductsMapper();
        List<String> requiredHeaders = mapper.getRequiredHeaders();
        assertEquals(5, requiredHeaders.size());
        assertTrue(requiredHeaders.contains("sales_orders_id"));
        assertTrue(requiredHeaders.contains("product_id"));
        assertTrue(requiredHeaders.contains("distributors_id"));
        assertTrue(requiredHeaders.contains("quantity"));
        assertTrue(requiredHeaders.contains("unitPrice"));
    }
}
