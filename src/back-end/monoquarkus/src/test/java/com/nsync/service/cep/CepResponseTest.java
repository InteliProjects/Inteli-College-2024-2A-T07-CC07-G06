package com.nsync.service.cep;

import com.nsync.entity.Distributor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit test for the {@link CepResponse} class.
 * This class contains several test cases to validate the initialization, behavior, and JSON serialization of the CepResponse model.
 */
@QuarkusTest
public class CepResponseTest {

    /**
     * Tests the constructor of the {@link CepResponse} class.
     * Verifies that the object is initialized with the correct attributes for the first day and last day.
     */
    @Test
    public void testConstructor() {
        LocalDate firstDay = LocalDate.now();
        LocalDate lastDay = LocalDate.now().plusDays(1);
        CepResponse response = new CepResponse(firstDay, lastDay);

        assertEquals(firstDay, response.firstDay);
        assertEquals(lastDay, response.lastDay);
    }

    /**
     * Tests the setter for the distributor in the {@link CepResponse} class.
     * Verifies that the distributor is set correctly.
     */
    @Test
    public void testSetDistributor() {
        Distributor distributor = new Distributor();
        CepResponse response = new CepResponse(LocalDate.now(), LocalDate.now().plusDays(1));
        response.setDistributor(distributor);

        assertEquals(distributor, response.distributor);
    }

    /**
     * Tests the JSON serialization of a {@link CepResponse} object.
     * Verifies that the object is serialized correctly to JSON format with all the required fields.
     * @throws Exception If there is an error during serialization.
     */
    @Test
    public void testJsonSerialization() throws Exception {
        LocalDate firstDay = LocalDate.now();
        LocalDate lastDay = LocalDate.now().plusDays(1);
        Distributor distributor = new Distributor();
        distributor.cep = "12345678";
        distributor.name = "Distribuidor 1";

        CepResponse response = new CepResponse(firstDay, lastDay);
        response.setDistributor(distributor);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        String json = mapper.writeValueAsString(response);

        System.out.println("Generated JSON: " + json);

        assertTrue(json.contains("\"firstDay\":\"" + firstDay.toString() + "\""));
        assertTrue(json.contains("\"lastDay\":\"" + lastDay.toString() + "\""));
        assertTrue(json.contains("\"distributor\":{\"id\":null,\"createdAt\":null,\"updatedAt\":null,\"name\":\"Distribuidor 1\",\"cep\":\"12345678\"}"));
    }
}
