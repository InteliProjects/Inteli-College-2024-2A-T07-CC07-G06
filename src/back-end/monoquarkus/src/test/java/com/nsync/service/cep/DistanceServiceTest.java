package com.nsync.service.cep;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for the {@link DistanceService} class.
 */
@QuarkusTest
public class DistanceServiceTest {

    private final DistanceService distanceService = new DistanceService();

    /**
     * Tests the {@link DistanceService#calculateDistance(double, double, double, double)} method
     * with known coordinates (New York and Los Angeles).
     */
    @Test
    public void testCalculateDistance_knownCoordinates() {
        double lat1 = 40.7128; // New York
        double lon1 = -74.0060;
        double lat2 = 34.0522; // Los Angeles
        double lon2 = -118.2437;
        double expectedDistance = 3935.746254609722; // Approximate distance in kilometers

        double actualDistance = distanceService.calculateDistance(lat1, lon1, lat2, lon2);

        assertEquals(expectedDistance, actualDistance, 0.1);
    }

    /**
     * Tests the {@link DistanceService#calculateDistance(double, double, double, double)} method
     * with the same coordinates.
     */
    @Test
    public void testCalculateDistance_sameCoordinates() {
        double lat = 51.5074; // London
        double lon = -0.1278;

        double actualDistance = distanceService.calculateDistance(lat, lon, lat, lon);

        assertEquals(0.0, actualDistance, 0.1);
    }

    /**
     * Tests the {@link DistanceService#calculateDistance(double, double, double, double)} method
     * with coordinates on the equator.
     */
    @Test
    public void testCalculateDistance_equatorCoordinates() {
        double lat1 = 0.0; // Equator
        double lon1 = 0.0;
        double lat2 = 0.0;
        double lon2 = 90.0; // 90 degrees east

        double expectedDistance = 10007.54; // Approximate distance in kilometers (quarter of Earth's circumference)

        double actualDistance = distanceService.calculateDistance(lat1, lon1, lat2, lon2);

        assertEquals(expectedDistance, actualDistance, 0.1);
    }

    /**
     * Tests the {@link DistanceService#calculateDistance(double, double, double, double)} method
     * with coordinates at the poles.
     */
    @Test
    public void testCalculateDistance_poleCoordinates() {
        double lat1 = 90.0; // North Pole
        double lon1 = 0.0;
        double lat2 = -90.0; // South Pole
        double lon2 = 0.0;

        double expectedDistance = 20015.09; // Approximate distance in kilometers (half of Earth's circumference)

        double actualDistance = distanceService.calculateDistance(lat1, lon1, lat2, lon2);

        assertEquals(expectedDistance, actualDistance, 0.1);
    }
}