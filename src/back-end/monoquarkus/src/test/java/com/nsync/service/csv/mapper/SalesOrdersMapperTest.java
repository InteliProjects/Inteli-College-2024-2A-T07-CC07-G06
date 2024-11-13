package com.nsync.service.csv.mapper;

import com.nsync.entity.SalesOrders;
import com.nsync.enums.SalesOrdersStatusEnum;
import com.nsync.exception.custom.MissingCsvFieldException;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@QuarkusTest
public class SalesOrdersMapperTest {

    private final SalesOrdersMapper mapper = new SalesOrdersMapper();

    @Test
    public void testMapToEntity_Success() {
        Map<String, String> csvRecord = new HashMap<>();
        csvRecord.put("customerCep", "12345");
        csvRecord.put("saleDate", "2023-10-01T10:15:30");
        csvRecord.put("total", "100.50");
        csvRecord.put("status", "COMPLETED");
        csvRecord.put("firstDeliveryDate", "2023-10-05");
        csvRecord.put("lastDeliveryDate", "2023-10-10");

        SalesOrders salesOrders = mapper.mapToEntity(csvRecord);

        assertEquals("12345", salesOrders.customerCep);
        assertEquals(LocalDateTime.of(2023, 10, 1, 10, 15, 30), salesOrders.saleDate);
        assertEquals(100.50, salesOrders.total);
        assertEquals(SalesOrdersStatusEnum.COMPLETED, salesOrders.status);
        assertEquals(LocalDate.of(2023, 10, 5), salesOrders.firstDeliveryDate);
        assertEquals(LocalDate.of(2023, 10, 10), salesOrders.lastDeliveryDate);
    }

    @Test
    public void testMapToEntity_MissingFields() {
        Map<String, String> csvRecord = new HashMap<>();
        csvRecord.put("customerCep", "12345");
        csvRecord.put("saleDate", "2023-10-01T10:15:30");

        Executable executable = () -> mapper.mapToEntity(csvRecord);
        MissingCsvFieldException exception = assertThrows(MissingCsvFieldException.class, executable);
        assertTrue(exception.getMessage().contains("Campos faltando no CSV"));
    }

    @Test
    public void testMapToEntity_InvalidDateFormat() {
        Map<String, String> csvRecord = new HashMap<>();
        csvRecord.put("customerCep", "12345");
        csvRecord.put("saleDate", "invalid-date");
        csvRecord.put("total", "100.50");
        csvRecord.put("status", "COMPLETED");
        csvRecord.put("firstDeliveryDate", "2023-10-05");
        csvRecord.put("lastDeliveryDate", "2023-10-10");

        Executable executable = () -> mapper.mapToEntity(csvRecord);
        assertThrows(Exception.class, executable);
    }

    @Test
    public void testGetEntityName() {
        assertEquals("SalesOrders", mapper.getEntityName());
    }

    @Test
    public void testGetRequiredHeaders() {
        List<String> expectedHeaders = List.of("customerCep", "saleDate", "total", "status", "firstDeliveryDate", "lastDeliveryDate");
        assertEquals(expectedHeaders, mapper.getRequiredHeaders());
    }

    @Test
    public void testMapToEntity_EmptyFields() {
        Map<String, String> csvRecord = new HashMap<>();
        csvRecord.put("customerCep", "");
        csvRecord.put("saleDate", "");
        csvRecord.put("total", "");
        csvRecord.put("status", "");
        csvRecord.put("firstDeliveryDate", "");
        csvRecord.put("lastDeliveryDate", "");

        Executable executable = () -> mapper.mapToEntity(csvRecord);
        assertThrows(DateTimeParseException.class, executable);
    }

    @Test
    public void testMapToEntity_InvalidTotalFormat() {
        Map<String, String> csvRecord = new HashMap<>();
        csvRecord.put("customerCep", "12345");
        csvRecord.put("saleDate", "2023-10-01T10:15:30");
        csvRecord.put("total", "invalid-total");
        csvRecord.put("status", "COMPLETED");
        csvRecord.put("firstDeliveryDate", "2023-10-05");
        csvRecord.put("lastDeliveryDate", "2023-10-10");

        Executable executable = () -> mapper.mapToEntity(csvRecord);
        assertThrows(NumberFormatException.class, executable);
    }

    @Test
    public void testMapToEntity_InvalidStatus() {
        Map<String, String> csvRecord = new HashMap<>();
        csvRecord.put("customerCep", "12345");
        csvRecord.put("saleDate", "2023-10-01T10:15:30");
        csvRecord.put("total", "100.50");
        csvRecord.put("status", "INVALID_STATUS");
        csvRecord.put("firstDeliveryDate", "2023-10-05");
        csvRecord.put("lastDeliveryDate", "2023-10-10");

        Executable executable = () -> mapper.mapToEntity(csvRecord);
        assertThrows(IllegalArgumentException.class, executable);
    }
}