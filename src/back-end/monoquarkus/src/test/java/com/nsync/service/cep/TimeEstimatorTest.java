package com.nsync.service.cep;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit test for the {@link TimeEstimator} class.
 * This class contains several test cases to validate the delivery date calculation logic based on distance.
 */
public class TimeEstimatorTest {

    /**
     * Subclass for testing purposes that returns a fixed date.
     * Overrides the method to provide a mock date instead of the current date.
     */
    class TestableTimeEstimator extends TimeEstimator {
        private final LocalDate mockToday;

        /**
         * Constructor for initializing with a fixed date.
         * @param mockToday The mock date to use for testing.
         */
        public TestableTimeEstimator(LocalDate mockToday) {
            this.mockToday = mockToday;
        }

        /**
         * Overrides the method to return a fixed date for testing.
         * @return The fixed date provided during instantiation.
         */
        @Override
        protected LocalDate getToday() {
            return mockToday;  // Returns the fixed date for testing
        }
    }

    /**
     * Tests the calculation of delivery dates for distances less than 1 km.
     * Verifies that the first and last delivery dates are correctly calculated.
     */
    @Test
    public void testCalculateDeliveryDates_LessThan1Km() {
        LocalDate mockToday = LocalDate.of(2023, 10, 1); 
        TimeEstimator estimator = new TestableTimeEstimator(mockToday);

        CepResponse response = estimator.calculateDeliveryDates(0.5);

        // Validates the expected results
        assertEquals(LocalDate.of(2023, 10, 2), response.firstDay);
        assertEquals(LocalDate.of(2023, 10, 3), response.lastDay);
    }

    /**
     * Tests the calculation of delivery dates for distances less than 10 km.
     * Verifies that the first and last delivery dates are correctly calculated.
     */
    @Test
    public void testCalculateDeliveryDates_LessThan10Km() {
        LocalDate mockToday = LocalDate.of(2023, 10, 1); 
        TimeEstimator estimator = new TestableTimeEstimator(mockToday);

        CepResponse response = estimator.calculateDeliveryDates(5);

        // Validates the expected results
        assertEquals(LocalDate.of(2023, 10, 3), response.firstDay);
        assertEquals(LocalDate.of(2023, 10, 5), response.lastDay);
    }

    /**
     * Tests the calculation of delivery dates for distances less than 50 km.
     * Verifies that the first and last delivery dates are correctly calculated.
     */
    @Test
    public void testCalculateDeliveryDates_LessThan50Km() {
        LocalDate mockToday = LocalDate.of(2023, 10, 1); 
        TimeEstimator estimator = new TestableTimeEstimator(mockToday);

        CepResponse response = estimator.calculateDeliveryDates(20);

        // Validates the expected results
        assertEquals(LocalDate.of(2023, 10, 4), response.firstDay);
        assertEquals(LocalDate.of(2023, 10, 7), response.lastDay);
    }

    /**
     * Tests the calculation of delivery dates for distances more than 50 km.
     * Verifies that the first and last delivery dates are correctly calculated.
     */
    @Test
    public void testCalculateDeliveryDates_MoreThan50Km() {
        LocalDate mockToday = LocalDate.of(2023, 10, 1); 
        TimeEstimator estimator = new TestableTimeEstimator(mockToday);

        CepResponse response = estimator.calculateDeliveryDates(100);

        // Validates the expected results
        assertEquals(LocalDate.of(2023, 10, 5), response.firstDay);
        assertEquals(LocalDate.of(2023, 10, 9), response.lastDay);
    }
}
