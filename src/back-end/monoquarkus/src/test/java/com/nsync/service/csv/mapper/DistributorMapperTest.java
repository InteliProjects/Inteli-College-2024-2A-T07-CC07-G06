package com.nsync.service.csv.mapper;

import com.nsync.entity.Distributor;
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
 * Unit test for the {@link DistributorMapper} class.
 * This class contains several test cases to validate the mapping of CSV records to the Distributor entity.
 */
@QuarkusTest
public class DistributorMapperTest {

    /**
     * Tests the mapping of a valid CSV record to a {@link Distributor} entity.
     * Verifies that the entity is correctly created from a CSV record containing valid fields.
     */
    @Test
    public void testMapToEntity_ValidRecord() {
        DistributorMapper mapper = new DistributorMapper();
        Map<String, String> csvRecord = new HashMap<>();
        csvRecord.put("name", "DistributorName");
        csvRecord.put("cep", "12345");

        Distributor distributor = mapper.mapToEntity(csvRecord);

        assertNotNull(distributor);
        assertEquals("DistributorName", distributor.name);
        assertEquals("12345", distributor.cep);
    }

    /**
     * Tests the mapping of a CSV record with missing headers.
     * Verifies that a {@link MissingCsvFieldException} is thrown when a required field is missing.
     */
    @Test
    public void testMapToEntity_MissingHeaders() {
        DistributorMapper mapper = new DistributorMapper();
        Map<String, String> csvRecord = new HashMap<>();
        csvRecord.put("name", "DistributorName");

        Executable executable = () -> mapper.mapToEntity(csvRecord);

        MissingCsvFieldException exception = assertThrows(MissingCsvFieldException.class, executable);
        assertTrue(exception.getMessage().contains("Campos faltando no CSV: cep"));
    }

    /**
     * Tests the retrieval of the entity name in {@link DistributorMapper}.
     * Verifies that the entity name is correctly returned as "Distributor".
     */
    @Test
    public void testGetEntityName() {
        DistributorMapper mapper = new DistributorMapper();
        assertEquals("Distributor", mapper.getEntityName());
    }

    /**
     * Tests the retrieval of the required headers in {@link DistributorMapper}.
     * Verifies that the required headers list contains all the mandatory fields.
     */
    @Test
    public void testGetRequiredHeaders() {
        DistributorMapper mapper = new DistributorMapper();
        List<String> requiredHeaders = mapper.getRequiredHeaders();
        assertEquals(2, requiredHeaders.size());
        assertTrue(requiredHeaders.contains("name"));
        assertTrue(requiredHeaders.contains("cep"));
    }

    /**
     * Tests the mapping of an empty CSV record to a {@link Distributor} entity.
     * Verifies that a {@link MissingCsvFieldException} is thrown when all required fields are missing.
     */
    @Test
    public void testMapToEntity_EmptyRecord() {
        DistributorMapper mapper = new DistributorMapper();
        Map<String, String> csvRecord = new HashMap<>();

        Executable executable = () -> mapper.mapToEntity(csvRecord);

        MissingCsvFieldException exception = assertThrows(MissingCsvFieldException.class, executable);
        assertTrue(exception.getMessage().contains("Campos faltando no CSV: name, cep"));
    }

    /**
     * Tests the mapping of a null CSV record to a {@link Distributor} entity.
     * Verifies that a {@link NullPointerException} is thrown when the record is null.
     */
    @Test
    public void testMapToEntity_NullRecord() {
        DistributorMapper mapper = new DistributorMapper();

        Executable executable = () -> mapper.mapToEntity(null);

        assertThrows(NullPointerException.class, executable);
    }

    /**
     * Tests the mapping of a CSV record with extra fields to a {@link Distributor} entity.
     * Verifies that the entity is correctly created from a CSV record containing valid fields,
     * and extra fields are ignored.
     */
    @Test
    public void testMapToEntity_ExtraFields() {
        DistributorMapper mapper = new DistributorMapper();
        Map<String, String> csvRecord = new HashMap<>();
        csvRecord.put("name", "DistributorName");
        csvRecord.put("cep", "12345");
        csvRecord.put("extraField", "extraValue");

        Distributor distributor = mapper.mapToEntity(csvRecord);

        assertNotNull(distributor);
        assertEquals("DistributorName", distributor.name);
        assertEquals("12345", distributor.cep);
    }
}
