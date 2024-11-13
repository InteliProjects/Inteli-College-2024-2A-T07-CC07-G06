package com.nsync.service.csv.mapper;

import com.nsync.entity.Distributor;
import com.nsync.entity.Product;
import com.nsync.entity.SalesOrders;
import com.nsync.entity.SalesProducts;
import com.nsync.exception.custom.MissingCsvFieldException;
import com.nsync.generic.service.utils.CsvEntityMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Maps CSV records to {@link SalesProducts} entities.
 * <p>
 * This class implements the {@link CsvEntityMapper} interface and provides the mapping logic
 * for converting CSV records into {@link SalesProducts} entities. It defines the required headers
 * and the entity name associated with this mapper.
 * </p>
 *
 * @author mauroDasChagas
 */
public class SalesProductsMapper implements CsvEntityMapper<SalesProducts> {

    /**
     * Maps a CSV record to a {@link SalesProducts} entity.
     * <p>
     * This method creates a new {@link SalesProducts} instance and populates its fields using
     * the values from the provided CSV record map. The fields are mapped as follows:
     * <ul>
     *     <li>{@code sales_orders_id} - The ID of the sales order associated with the sales product.</li>
     *     <li>{@code product_id} - The ID of the product associated with the sales product.</li>
     *     <li>{@code quantity} - The quantity of the product in the sales order (converted to {@code Integer}).</li>
     *     <li>{@code unitPrice} - The unit price of the product (converted to {@code Double}).</li>
     * </ul>
     * </p>
     *
     * @param csvRecord a map representing a single CSV record, where keys are header names and values are the data.
     * @return the {@link SalesProducts} entity created from the CSV record.
     */
    @Override
    public SalesProducts mapToEntity(Map<String, String> csvRecord) {
        // Check for missing headers in the CSV
        List<String> missingFields = getRequiredHeaders().stream()
                .filter(header -> !csvRecord.containsKey(header))
                .toList();

        if (!missingFields.isEmpty()) {
            throw new MissingCsvFieldException("Campos faltando no CSV: " + String.join(", ", missingFields));
        }

        SalesProducts salesProducts = new SalesProducts();
        salesProducts.order = new SalesOrders();
        salesProducts.order.id = Long.parseLong(csvRecord.get("sale_order_id"));
        salesProducts.product = new Product();
        salesProducts.product.id = Long.parseLong(csvRecord.get("product_id"));
        salesProducts.distributor = new Distributor();
        salesProducts.distributor.id = Long.parseLong(csvRecord.get("distributor_id"));
        salesProducts.quantity = Integer.parseInt(csvRecord.get("quantity"));
        salesProducts.unitPrice = Double.parseDouble(csvRecord.get("unit_price"));
        return salesProducts;
    }

    /**
     * Returns the name of the entity associated with this mapper.
     * <p>
     * The entity name is used to locate the corresponding CSV file and is typically the name
     * of the entity class.
     * </p>
     *
     * @return the name of the entity, which is "SalesProducts".
     */
    @Override
    public String getEntityName() {
        return "SalesProducts";
    }

    /**
     * Returns the list of required headers for the CSV file.
     * <p>
     * These headers are used to ensure that the CSV data can be correctly mapped to the entity fields.
     * </p>
     *
     * @return a list of required header names, which are "sales_orders_id", "product_id", "distributors_id", "quantity", "unitPrice".
     */
    @Override
    public List<String> getRequiredHeaders() {
        return Arrays.asList("sale_order_id", "product_id", "distributor_id", "quantity", "unit_price");
    }
}
