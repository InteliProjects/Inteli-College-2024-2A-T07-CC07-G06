package com.nsync.service.csv.mapper;

import com.nsync.entity.ProductDistributor;
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

/**
 * Unit test for the {@link ProductDistributorMapper} class.
 * This class contains several test cases to validate the mapping of CSV records to the ProductDistributor entity.
 */
@QuarkusTest
public class ProductDistributorMapperTest {

    private final ProductDistributorMapper mapper = new ProductDistributorMapper();

    /**
     * Tests the mapping of a valid CSV record to a {@link ProductDistributor} entity.
     * Verifies that the entity is correctly created from a CSV record containing valid fields.
     */
    @Test
    public void testMapToEntity_ValidRecord() {
        Map<String, String> csvRecord = new HashMap<>();
        csvRecord.put("distributor_id", "1");
        csvRecord.put("product_id", "2");
        csvRecord.put("quantityAvailable", "100");
        csvRecord.put("quantityReserved", "50");

        ProductDistributor productDistributor = mapper.mapToEntity(csvRecord);

        assertNotNull(productDistributor);
        assertEquals(1L, productDistributor.distributor.id);
        assertEquals(2L, productDistributor.product.id);
        assertEquals(100, productDistributor.quantityAvailable);
        assertEquals(50, productDistributor.quantityReserved);
    }

    /**
     * Tests the mapping of a CSV record with missing headers.
     * Verifies that a {@link MissingCsvFieldException} is thrown when a required field is missing.
     */
    @Test
    public void testMapToEntity_MissingHeaders() {
        Map<String, String> csvRecord = new HashMap<>();
        csvRecord.put("distributor_id", "1");
        csvRecord.put("product_id", "2");

        Executable executable = () -> mapper.mapToEntity(csvRecord);

        MissingCsvFieldException exception = assertThrows(MissingCsvFieldException.class, executable);
        assertTrue(exception.getMessage().contains("Campos faltando no CSV"));
    }

    /**
     * Tests the mapping of a CSV record with invalid data types.
     * Verifies that a {@link NumberFormatException} is thrown when a field has an invalid data type.
     */
    @Test
    public void testMapToEntity_InvalidDataTypes() {
        Map<String, String> csvRecord = new HashMap<>();
        csvRecord.put("distributor_id", "invalid");
        csvRecord.put("product_id", "2");
        csvRecord.put("quantityAvailable", "100");
        csvRecord.put("quantityReserved", "50");

        Executable executable = () -> mapper.mapToEntity(csvRecord);

        assertThrows(NumberFormatException.class, executable);
    }

    /**
     * Tests the retrieval of the entity name in {@link ProductDistributorMapper}.
     * Verifies that the entity name is correctly returned as "ProductDistributor".
     */
    @Test
    public void testGetEntityName() {
        String entityName = mapper.getEntityName();
        assertEquals("ProductDistributor", entityName);
    }

    /**
     * Tests the retrieval of the required headers in {@link ProductDistributorMapper}.
     * Verifies that the required headers list contains all the mandatory fields.
     */
    @Test
    public void testGetRequiredHeaders() {
        List<String> requiredHeaders = mapper.getRequiredHeaders();
        assertEquals(List.of("distributor_id", "product_id", "quantityAvailable", "quantityReserved"), requiredHeaders);
    }

    /**
     * Tests the mapping of an empty CSV record to a {@link ProductDistributor} entity.
     * Verifies that a {@link MissingCsvFieldException} is thrown when all required fields are missing.
     */
    @Test
    public void testMapToEntity_EmptyRecord() {
        Map<String, String> csvRecord = new HashMap<>();

        Executable executable = () -> mapper.mapToEntity(csvRecord);

        MissingCsvFieldException exception = assertThrows(MissingCsvFieldException.class, executable);
        assertTrue(exception.getMessage().contains("Campos faltando no CSV"));
    }

    /**
     * Tests the mapping of a CSV record with null values.
     * Verifies that a {@link NumberFormatException} is thrown when a required field has a null value.
     */
    @Test
    public void testMapToEntity_NullValues() {
        Map<String, String> csvRecord = new HashMap<>();
        csvRecord.put("distributor_id", null);
        csvRecord.put("product_id", "2");
        csvRecord.put("quantityAvailable", "100");
        csvRecord.put("quantityReserved", "50");

        Executable executable = () -> mapper.mapToEntity(csvRecord);

        assertThrows(NumberFormatException.class, executable);
    }

    /**
     * Tests the mapping of a CSV record with extra headers to a {@link ProductDistributor} entity.
     * Verifies that the entity is correctly created from a CSV record containing valid fields,
     * and extra fields are ignored.
     */
    @Test
    public void testMapToEntity_ExtraHeaders() {
        Map<String, String> csvRecord = new HashMap<>();
        csvRecord.put("distributor_id", "1");
        csvRecord.put("product_id", "2");
        csvRecord.put("quantityAvailable", "100");
        csvRecord.put("quantityReserved", "50");
        csvRecord.put("extra_field", "extra_value");

        ProductDistributor productDistributor = mapper.mapToEntity(csvRecord);

        assertNotNull(productDistributor);
        assertEquals(1L, productDistributor.distributor.id);
        assertEquals(2L, productDistributor.product.id);
        assertEquals(100, productDistributor.quantityAvailable);
        assertEquals(50, productDistributor.quantityReserved);
    }
}
