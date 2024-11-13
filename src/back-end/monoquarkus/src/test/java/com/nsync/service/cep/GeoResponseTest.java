package com.nsync.service.cep;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for the {@link GeoResponse} class.
 */
@QuarkusTest
public class GeoResponseTest {

    /**
     * Tests the {@link GeoResponse#setLatitude(double)} and {@link GeoResponse#getLatitude()} methods.
     * Verifies that the latitude value is correctly set and retrieved.
     * The latitude value is set to 45.0 and then retrieved to validate the result.
     */
    @Test
    public void testSetAndGetLatitude() {
        GeoResponse geoResponse = new GeoResponse();
        geoResponse.setLatitude(45.0);
        assertEquals(45.0, geoResponse.getLatitude(), 0.001);
    }

    /**
     * Tests the {@link GeoResponse#setLongitude(double)} and {@link GeoResponse#getLongitude()} methods.
     * Verifies that the longitude value is correctly set and retrieved.
     * The longitude value is set to 90.0 and then retrieved to validate the result.
     */
    @Test
    public void testSetAndGetLongitude() {
        GeoResponse geoResponse = new GeoResponse();
        geoResponse.setLongitude(90.0);
        assertEquals(90.0, geoResponse.getLongitude(), 0.001);
    }

    /**
     * Tests the {@link GeoResponse#toString()} method.
     * Verifies that the method returns the expected string representation of the object.
     */
    @Test
    public void testToString() {
        GeoResponse geoResponse = new GeoResponse();
        geoResponse.setLatitude(45.0);
        geoResponse.setLongitude(90.0);
        String expected = "GeoResponse{latitude=45.0, longitude=90.0}";
        assertEquals(expected, geoResponse.toString());
    }
}