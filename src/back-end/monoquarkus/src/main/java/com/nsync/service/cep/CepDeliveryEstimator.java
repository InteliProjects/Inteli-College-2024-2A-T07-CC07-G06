package com.nsync.service.cep;

import com.nsync.entity.Distributor;
import com.nsync.models.ProductRequest;
import com.nsync.models.PurchaseRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Mocked service for estimating delivery days based on a CEP (Postal Code).
 * <p>
 * This service provides a mock implementation for calculating estimated delivery
 * dates based on a given CEP. The dates are generated randomly to simulate
 * delivery time estimates for testing purposes. This is not a real delivery
 * estimation service and is meant for use in a development or testing environment.
 * </p>
 *
 * @author ColettoG
 */
@ApplicationScoped
public class CepDeliveryEstimator {
    private final Random random = new Random();
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Calculates estimated delivery days based on the provided CEP.
     * <p>
     * Generates a mock estimate for the first and last day of delivery based
     * on random values. The returned dates are not based on real data but are
     * intended to simulate delivery timeframes for the purpose of testing.
     * </p>
     *
     * @param cep the postal code (CEP) used to estimate delivery days.
     * @return a {@code CepResponse} containing the estimated first and last
     *         delivery days.
     */
    public CepResponse calculateDays(String cep, Long productId, Integer quantity, boolean mock, boolean includeDistributor ) {

        if (entityManager == null) {
            System.out.println();
        }
        // Selecting just the distribuitors who has the product
        List<Distributor> distributors = entityManager
                .createQuery("SELECT d FROM Distributor d " +
                                "JOIN ProductDistributor pd ON pd.distributor = d " +
                                "WHERE pd.product.id = :productId AND pd.quantityAvailable >= :quantity",
                        Distributor.class)
                .setParameter("productId", productId)
                .setParameter("quantity", quantity)
                .setMaxResults(10)
                .getResultList();

        if (mock) {
            int firstDayRandom = random.nextInt(1, 5);
            int lastDayRandom = firstDayRandom + random.nextInt(1, 10);

            LocalDate firstDay = LocalDate.now().plusDays(firstDayRandom);
            LocalDate lastDay = LocalDate.now().plusDays(lastDayRandom);

            CepResponse cepResponse = new CepResponse(firstDay, lastDay);

            if (includeDistributor) {
                cepResponse.setDistributor(distributors.get(0));
            }

            return cepResponse;
        }

        // Transform cep into latitude and longitude
        GeoData geoData = new GeoData();
        GeoResponse coordCustomer = geoData.getGeoData(cep);
        List<GeoResponse> coordsDistributors = new ArrayList<>();

        double latCustomer = coordCustomer.getLatitude();
        double lonCustomer = coordCustomer.getLongitude();

        DistanceService distanceService = new DistanceService();

        double shortestDistance = Double.MAX_VALUE;
        Distributor nearestDistributor = null;

        for (Distributor distributor: distributors) {
            // get the geoData
            GeoResponse coordDistributor = geoData.getGeoData(distributor.cep);
            // calculate distance
            double latDistributor = coordDistributor.getLatitude();
            double lonDistributor = coordDistributor.getLongitude();

            double kmDistance = distanceService.calculateDistance(latCustomer, lonCustomer, latDistributor, lonDistributor);

            // if the distance is the shortest add to the shortest and nearest variable
            if (kmDistance < shortestDistance) {
                shortestDistance = kmDistance;
                nearestDistributor = distributor;
            }
        }

        TimeEstimator timeEstimator = new TimeEstimator();

        CepResponse response = timeEstimator.calculateDeliveryDates(shortestDistance);

        if (includeDistributor && nearestDistributor != null) {
            response.setDistributor(nearestDistributor);
        }

        return response;
    }
    public CepResponse calculateDays(String cep, Long productId) {
        return calculateDays(cep, productId, 1, false, false);
    }

    public List<CepPurchaseResponse> calculateDaysPurchase(PurchaseRequest purchaseRequest) {
        String cep = purchaseRequest.getCep();
        List<ProductRequest> productRequestList = purchaseRequest.getProducts();
        List<CepPurchaseResponse> cepResponseList = new ArrayList<>();
        for (ProductRequest productRequest : productRequestList) {
            Long productId = productRequest.getProductId();
            Integer quantity = productRequest.getQuantity();

            CepResponse deliveryDays = calculateDaysPurchase(cep, productId, quantity, true);
            Distributor distributor = deliveryDays.distributor;

            CepPurchaseResponse cepPurchaseResponse = new CepPurchaseResponse(deliveryDays.firstDay, deliveryDays.lastDay, productId, distributor);
            cepResponseList.add(cepPurchaseResponse);
        }

        return cepResponseList;
    }
    public CepResponse calculateDaysPurchase(String cep, Long productId, Integer quantity, boolean mock) {
        return calculateDays(cep, productId, quantity, mock, true);
    }
}
