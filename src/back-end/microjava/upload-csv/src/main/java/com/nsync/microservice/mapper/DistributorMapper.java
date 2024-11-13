package com.nsync.microservice.mapper;

import com.nsync.microservice.entity.Distributor;
import com.nsync.microservice.exception.custom.MissingCsvFieldException;
import com.nsync.microservice.utils.CsvEntityMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Maps CSV records to {@link Distributor} entities.
 * <p>
 * This class implements the {@link CsvEntityMapper} interface and provides the mapping logic
 * for converting CSV records into {@link Distributor} entities. It defines the required headers
 * and the entity name associated with this mapper.
 * </p>
 *
 * @author mauroDasChagas
 */
public class DistributorMapper implements CsvEntityMapper<Distributor> {

    /**
     * Maps a CSV record to a {@link Distributor} entity.
     * <p>
     * This method creates a new {@link Distributor} instance and populates its fields using
     * the values from the provided CSV record map.
     * </p>
     *
     * @param csvRecord a map representing a single CSV record, where keys are header names and values are the data.
     * @return the {@link Distributor} entity created from the CSV record.
     */
    @Override
    public Distributor mapToEntity(Map<String, String> csvRecord) {
        // Check for missing headers in the CSV
        List<String> missingFields = getRequiredHeaders().stream()
                .filter(header -> !csvRecord.containsKey(header))
                .toList();

        if (!missingFields.isEmpty()) {
            throw new MissingCsvFieldException("Campos faltando no CSV: " + String.join(", ", missingFields));
        }

        Distributor distributor = new Distributor();
        distributor.name = csvRecord.get("name");
        distributor.cep = csvRecord.get("cep");
        return distributor;
    }

    /**
     * Returns the name of the entity associated with this mapper.
     * <p>
     * The entity name is used to locate the corresponding CSV file and is typically the name
     * of the entity class.
     * </p>
     *
     * @return the name of the entity, which is "Distributor".
     */
    @Override
    public String getEntityName() {
        return "Distributor";
    }

    /**
     * Returns the list of required headers for the CSV file.
     * <p>
     * These headers are used to ensure that the CSV data can be correctly mapped to the entity fields.
     * </p>
     *
     * @return a list of required header names, which are "name" and "cep".
     */
    @Override
    public List<String> getRequiredHeaders() {
        return Arrays.asList("name", "cep");
    }
}
