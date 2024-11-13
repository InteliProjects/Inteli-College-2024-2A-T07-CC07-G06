package com.nsync.microservice.generic.entity;

import com.nsync.microservice.exception.custom.MissingCsvFieldException;
import com.nsync.microservice.utils.CsvEntityMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Service class for processing CSV files and populating the database with entities.
 * <p>
 * This service reads a CSV file from the classpath, parses it, and maps the CSV records to entity objects
 * using a {@link CsvEntityMapper}. It then persists the entities to the database.
 * </p>
 *
 * @author mauroDasChagas
 */
@ApplicationScoped
public class GenericCsvProcessorService {

    @Inject
    EntityManager entityManager;

    /**
     * Processes a CSV file to populate the database with entities.
     * <p>
     * This method reads a CSV file named {@code default_<entityName>.csv} from the classpath, where {@code <entityName>}
     * is determined by the {@code CsvEntityMapper} provided. It parses the CSV file, maps each record to an entity
     * using the provided {@code CsvEntityMapper}, and persists each entity to the database.
     * </p>
     *
     * @param mapper the {@link CsvEntityMapper} used to map CSV records to entities and to provide the entity name and required headers.
     * @param <T> the type of the entity being processed.
     *
     * @throws IOException if an I/O error occurs while reading the CSV file.
     * @throws RuntimeException if an error occurs during entity persistence (e.g., database constraint violation).
     */
    @Transactional
    public <T> void processCsvFile(InputStream inputStream, CsvEntityMapper<T> mapper) {
        try (InputStreamReader reader = new InputStreamReader(inputStream);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {

            // Verify headers
            verifyHeaders(csvParser, mapper);

            // Process records
            for (CSVRecord record : csvParser) {
                processRecord(record, mapper);
            }

        } catch (IOException e) {
            throw new RuntimeException("Error processing CSV file", e);
        }
    }

    private <T> void verifyHeaders(CSVParser csvParser, CsvEntityMapper<T> mapper) {
        Map<String, Integer> headerMap = csvParser.getHeaderMap();
        System.out.println("Headers found in CSV: " + headerMap.keySet());
        System.out.println("Required headers: " + mapper.getRequiredHeaders());

        Set<String> missingHeaders = new HashSet<>();
        for (String header : mapper.getRequiredHeaders()) {
            if (!headerMap.containsKey(header)) {
                missingHeaders.add(header);
            }
        }

        if (!missingHeaders.isEmpty()) {
            throw new MissingCsvFieldException("Missing values for headers: " + String.join(", ", missingHeaders));
        }
    }

    private <T> void processRecord(CSVRecord record, CsvEntityMapper<T> mapper) {
        try {
            Map<String, String> recordMap = new HashMap<>();
            for (String header : mapper.getRequiredHeaders()) {
                String value = record.get(header);
                recordMap.put(header, value);
            }

            T entity = mapper.mapToEntity(recordMap);
            entityManager.persist(entity);
        } catch (Exception e) {
            // Log or handle the error with the specific record
            throw new RuntimeException("Error processing record: " + record.toString(), e);
        }
    }

}
