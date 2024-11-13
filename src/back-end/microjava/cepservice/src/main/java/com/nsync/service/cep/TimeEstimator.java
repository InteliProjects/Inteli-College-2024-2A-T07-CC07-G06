package com.nsync.service.cep;

import java.time.LocalDate;

/**
 * Service class to estimate delivery dates based on distance.
 * <p>
 * Calculates the first and last possible delivery dates for a given distance.
 * </p>
 *
 * @author Coletto
 */
public class TimeEstimator {

    /**
     * Calculates the estimated delivery dates based on the provided distance.
     *
     * @param distance the distance in kilometers for the delivery.
     * @return a {@link CepResponse} object containing the first and last delivery dates.
     */
    public CepResponse calculateDeliveryDates(double distance) {
        int daysToDeliver;

        if (distance < 1) {
            daysToDeliver = 1;
        } else if (distance < 10) {
            daysToDeliver = 2;
        } else if (distance < 50) {
            daysToDeliver = 3;
        } else {
            daysToDeliver = 4;
        }

        LocalDate today = LocalDate.now();
        LocalDate firstDeliveryDate = today.plusDays(daysToDeliver);
        LocalDate lastDeliveryDate = firstDeliveryDate.plusDays(daysToDeliver); // Adding a delivery window

        return new CepResponse(firstDeliveryDate, lastDeliveryDate);
    }
}
