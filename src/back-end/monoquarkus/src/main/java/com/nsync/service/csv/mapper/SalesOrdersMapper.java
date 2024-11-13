package com.nsync.service.csv.mapper;

import com.nsync.entity.SalesOrders;
import com.nsync.exception.custom.MissingCsvFieldException;
import com.nsync.generic.service.utils.CsvEntityMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Maps CSV records to {@link SalesOrders} entities.
 * <p>
 * This class implements the {@link CsvEntityMapper} interface and provides the mapping logic
 * for converting CSV records into {@link SalesOrders} entities. It defines the required headers
 * and the entity name associated with this mapper.
 * </p>
 *
 * @author mauroDasChagas
 */
public class SalesOrdersMapper implements CsvEntityMapper<SalesOrders> {

    /**
     * Maps a CSV record to a {@link SalesOrders} entity.
     * <p>
     * This method creates a new {@link SalesOrders} instance and populates its fields using
     * the values from the provided CSV record map. The fields are mapped as follows:
     * <ul>
     *     <li>{@code distributor_id} - The ID of the distributor associated with the sales order.</li>
     *     <li>{@code saleDate} - The date and time of the sale (parsed as {@code LocalDateTime}).</li>
     *     <li>{@code total} - The total amount of the sales order (converted to {@code Double}).</li>
     *     <li>{@code status} - The status of the sales order.</li>
     *     <li>{@code deliveryDate} - The date and time when the order is expected to be delivered (parsed as {@code LocalDateTime}).</li>
     * </ul>
     * </p>
     *
     * @param csvRecord a map representing a single CSV record, where keys are header names and values are the data.
     * @return the {@link SalesOrders} entity created from the CSV record.
     */
    @Override
    public SalesOrders mapToEntity(Map<String, String> csvRecord) {
        // Check for missing headers at the csvs
        List<String> missingFields = getRequiredHeaders().stream()
                .filter(header -> !csvRecord.containsKey(header))
                .toList();

        if (!missingFields.isEmpty()) {
            throw new MissingCsvFieldException("Campos faltando no CSV: " + String.join(", ", missingFields));
        }

        SalesOrders salesOrders = new SalesOrders();
        salesOrders.customerCep = csvRecord.get("customer_cep");
        salesOrders.saleDate = LocalDateTime.parse(csvRecord.get("sale_date"), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        salesOrders.total = Double.parseDouble(csvRecord.get("total"));
        salesOrders.firstDeliveryDate = LocalDate.parse(csvRecord.get("first_delivery_date"), DateTimeFormatter.ISO_LOCAL_DATE);
        salesOrders.lastDeliveryDate = LocalDate.parse(csvRecord.get("last_delivery_date"), DateTimeFormatter.ISO_LOCAL_DATE);

        return salesOrders;
    }

    /**
     * Returns the name of the entity associated with this mapper.
     * <p>
     * The entity name is used to locate the corresponding CSV file and is typically the name
     * of the entity class.
     * </p>
     *
     * @return the name of the entity, which is "SalesOrders".
     */
    @Override
    public String getEntityName() {
        return "SalesOrders";
    }

    /**
     * Returns the list of required headers for the CSV file.
     * <p>
     * These headers are used to ensure that the CSV data can be correctly mapped to the entity fields.
     * </p>
     *
     * @return a list of required header names, which are "customerCep", "saleDate", "total", "status", "firstDeliveryDate", "lastDeliveryDate".
     */
    @Override
    public List<String> getRequiredHeaders() {
        return Arrays.asList("customer_cep", "sale_date", "total", "first_delivery_date", "last_delivery_date");
    }
}