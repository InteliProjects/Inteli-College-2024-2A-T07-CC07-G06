package com.nsync.generic.service.utils;

import java.util.List;
import java.util.Map;

/**
 * Interface for mapping CSV records to entity objects.
 * <p>
 * Implementations of this interface provide the logic for mapping CSV data to entities,
 * including defining the entity name and required headers for CSV parsing.
 * </p>
 *
 * @author mauroDasChagas
 *
 * @param <T> the type of the entity that is mapped from the CSV record.
 */
public interface CsvEntityMapper<T> {

    /**
     * Maps a CSV record to an entity object.
     * <p>
     * This method takes a map of CSV record values, where the keys are the CSV headers
     * and the values are the corresponding data, and converts it into an entity object.
     * </p>
     *
     * @param csvRecord a map representing a single CSV record, where keys are header names and values are the data.
     * @return the entity object created from the CSV record.
     */
    T mapToEntity(Map<String, String> csvRecord);

    /**
     * Returns the name of the entity associated with this mapper.
     * <p>
     * This name is typically used to locate the corresponding CSV file in the classpath.
     * </p>
     *
     * @return the name of the entity.
     */
    String getEntityName();

    /**
     * Returns the list of required headers for the CSV file.
     * <p>
     * These headers are used to map the CSV data to the entity fields correctly.
     * </p>
     *
     * @return a list of required header names.
     */
    List<String> getRequiredHeaders();
}
