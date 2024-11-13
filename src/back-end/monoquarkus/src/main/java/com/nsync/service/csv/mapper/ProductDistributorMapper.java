package com.nsync.service.csv.mapper;

import com.nsync.entity.Distributor;
import com.nsync.entity.Product;
import com.nsync.entity.ProductDistributor;
import com.nsync.exception.custom.MissingCsvFieldException;
import com.nsync.generic.service.utils.CsvEntityMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Maps CSV records to {@link ProductDistributor} entities.
 * <p>
 * This class implements the {@link CsvEntityMapper} interface and provides the mapping logic
 * for converting CSV records into {@link ProductDistributor} entities. It defines the required headers
 * and the entity name associated with this mapper.
 * </p>
 *
 * @author mauroDasChagas
 */
public class ProductDistributorMapper implements CsvEntityMapper<ProductDistributor> {

    /**
     * Maps a CSV record to a {@link ProductDistributor} entity.
     * <p>
     * This method creates a new {@link ProductDistributor} instance and populates its fields using
     * the values from the provided CSV record map. It also initializes {@link Distributor} and {@link Product}
     * instances with their respective IDs from the CSV record.
     * </p>
     *
     * @param csvRecord a map representing a single CSV record, where keys are header names and values are the data.
     * @return the {@link ProductDistributor} entity created from the CSV record.
     */
    @Override
    public ProductDistributor mapToEntity(Map<String, String> csvRecord) {
        // Check for missing headers in the CSV
        List<String> missingFields = getRequiredHeaders().stream()
                .filter(header -> !csvRecord.containsKey(header))
                .toList();

        if (!missingFields.isEmpty()) {
            throw new MissingCsvFieldException("Campos faltando no CSV: " + String.join(", ", missingFields));
        }

        ProductDistributor productDistributor = new ProductDistributor();
        productDistributor.distributor = new Distributor();
        productDistributor.distributor.id = Long.parseLong(csvRecord.get("distributor_id"));
        productDistributor.product = new Product();
        productDistributor.product.id = Long.parseLong(csvRecord.get("product_id"));
        productDistributor.quantityAvailable = Integer.parseInt(csvRecord.get("quantity_available"));
        productDistributor.quantityReserved = Integer.parseInt(csvRecord.get("quantity_reserved"));
        return productDistributor;
    }

    /**
     * Returns the name of the entity associated with this mapper.
     * <p>
     * The entity name is used to locate the corresponding CSV file and is typically the name
     * of the entity class.
     * </p>
     *
     * @return the name of the entity, which is "ProductDistributor".
     */
    @Override
    public String getEntityName() {
        return "ProductDistributor";
    }

    /**
     * Returns the list of required headers for the CSV file.
     * <p>
     * These headers are used to ensure that the CSV data can be correctly mapped to the entity fields.
     * </p>
     *
     * @return a list of required header names, which are "distributor_id", "product_id", "quantityAvailable", and "quantityReserved".
     */
    @Override
    public List<String> getRequiredHeaders() {
        return Arrays.asList("distributor_id", "product_id", "quantity_available", "quantity_reserved");
    }
}
