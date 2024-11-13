package com.nsync.microservice.mapper;

import com.nsync.microservice.entity.Product;
import com.nsync.microservice.exception.custom.MissingCsvFieldException;
import com.nsync.microservice.utils.CsvEntityMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Maps CSV records to {@link Product} entities.
 * <p>
 * This class implements the {@link CsvEntityMapper} interface and provides the mapping logic
 * for converting CSV records into {@link Product} entities. It defines the required headers
 * and the entity name associated with this mapper.
 * </p>
 *
 * @author mauroDasChagas
 */
public class ProductMapper implements CsvEntityMapper<Product> {

    /**
     * Maps a CSV record to a {@link Product} entity.
     * <p>
     * This method creates a new {@link Product} instance and populates its fields using
     * the values from the provided CSV record map. The fields are mapped as follows:
     * <ul>
     *     <li>{@code sku} - The stock keeping unit of the product.</li>
     *     <li>{@code name} - The name of the product.</li>
     *     <li>{@code price} - The price of the product (converted to {@code Double}).</li>
     *     <li>{@code description} - A description of the product.</li>
     *     <li>{@code linkImage} - A URL link to the product image.</li>
     * </ul>
     * </p>
     *
     * @param csvRecord a map representing a single CSV record, where keys are header names and values are the data.
     * @return the {@link Product} entity created from the CSV record.
     */
    @Override
    public Product mapToEntity(Map<String, String> csvRecord) {
        // Check for missing headers in the CSV
        List<String> missingFields = getRequiredHeaders().stream()
                .filter(header -> !csvRecord.containsKey(header))
                .toList();

        if (!missingFields.isEmpty()) {
            throw new MissingCsvFieldException("Campos faltando no CSV: " + String.join(", ", missingFields));
        }

        Product product = new Product();
        product.sku = csvRecord.get("sku");
        product.name = csvRecord.get("name");
        product.price = Double.parseDouble(csvRecord.get("price"));
        product.description = csvRecord.get("description");
        product.linkImage = csvRecord.get("link_image");
        return product;
    }

    /**
     * Returns the name of the entity associated with this mapper.
     * <p>
     * The entity name is used to locate the corresponding CSV file and is typically the name
     * of the entity class.
     * </p>
     *
     * @return the name of the entity, which is "Product".
     */
    @Override
    public String getEntityName() {
        return "Product";
    }

    /**
     * Returns the list of required headers for the CSV file.
     * <p>
     * These headers are used to ensure that the CSV data can be correctly mapped to the entity fields.
     * </p>
     *
     * @return a list of required header names, which are "sku", "name", "price", "description", and "linkImage".
     */
    @Override
    public List<String> getRequiredHeaders() {
        return Arrays.asList("sku", "name", "price", "description", "link_image");
    }
}
