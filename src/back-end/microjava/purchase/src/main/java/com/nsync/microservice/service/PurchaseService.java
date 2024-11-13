package com.nsync.microservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nsync.microservice.entity.Distributor;
import com.nsync.microservice.entity.ProductDistributor;
import com.nsync.microservice.model.CepPurchaseResponse;
import com.nsync.microservice.model.ProductRequest;
import com.nsync.microservice.model.PurchaseRequest;
import com.nsync.microservice.model.ResponseWrapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class PurchaseService {

    @Inject
    public EntityManager entityManager;

    private final HttpClient httpClient = HttpClient.newHttpClient();

    // Replace the local call with HTTP request
    public List<CepPurchaseResponse> callCepDeliveryEstimator(PurchaseRequest purchaseRequest) throws Exception {
        // Prepare request
        ObjectMapper mapper = new ObjectMapper();
        String requestBody = mapper.writeValueAsString(purchaseRequest);

//                .uri(new URI("http://localhost:8080/cep/calculate-days-purchase"))
// .uri(new URI("http://cep-estimator:8082/cep/calculate-days-purchase"))

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://cep-estimator:8082/cep/calculate-days-purchase"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        // Send request and get response
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        String jsonResponse = response.body();
        System.out.println(response.body());

        try {
            // Manually convert JSON to List<CepPurchaseResponse>
            return mapJsonToPurchaseResponseList(jsonResponse);
        } catch (Exception e) {
            // Log the error details
            System.err.println("Error during deserialization: " + e.getMessage());
            e.printStackTrace();
            throw e; // Re-throw the exception after logging
        }
    }

    private List<CepPurchaseResponse> mapJsonToPurchaseResponseList(String jsonResponse) throws IOException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        // Parse the JSON string into a JsonNode
        JsonNode rootNode = objectMapper.readTree(jsonResponse);

        // Initialize the list that will store the response objects
        List<CepPurchaseResponse> purchaseResponses = new ArrayList<>();

        // Extract the "data" array node
        JsonNode dataArrayNode = rootNode.get("data");

        if (dataArrayNode.isArray()) {
            // Iterate over each element in the "data" array
            for (JsonNode itemNode : dataArrayNode) {
                // Map the distributor object
                JsonNode distributorNode = itemNode.get("distributor");
                Distributor distributor = new Distributor();
                distributor.id = distributorNode.get("id").asLong();
                distributor.cep = distributorNode.get("cep").asText();
                distributor.name = distributorNode.get("name").asText();
                LocalDate firstDay = LocalDate.parse(itemNode.get("firstDay").asText());
                LocalDate lastDay = LocalDate.parse(itemNode.get("lastDay").asText());
                Long productId = itemNode.get("productId").asLong();
                CepPurchaseResponse response = new CepPurchaseResponse(firstDay, lastDay, productId, distributor);

                // Add the response to the list
                purchaseResponses.add(response);
            }
        }

        return purchaseResponses;
    }


    @Transactional
    public boolean processPurchase(PurchaseRequest purchaseRequest) {
        List<ProductRequest> productRequestList = purchaseRequest.getProducts();
        try {
            // Call the external CepDeliveryEstimator microservice
            List<CepPurchaseResponse> cepPurchaseResponseList = callCepDeliveryEstimator(purchaseRequest);

            for (int i = 0; i < cepPurchaseResponseList.size(); i++) {
                Long productId = cepPurchaseResponseList.get(i).getProductId();
                Distributor distributor = cepPurchaseResponseList.get(i).getDistributor();
                Integer quantity = productRequestList.get(i).getQuantity();

                // Query to find the ProductDistributor based on productId and distributor
                ProductDistributor productDistributor = entityManager.createQuery(
                                "SELECT pd FROM ProductDistributor pd WHERE pd.product.id = :productId AND pd.distributor = :distributor",
                                ProductDistributor.class)
                        .setParameter("productId", productId)
                        .setParameter("distributor", distributor)
                        .getSingleResult();

                if (productDistributor == null || productDistributor.quantityAvailable < quantity) {
                    // If any purchase request fails, return false
                    return false;
                }

                // Deduct the quantity
                productDistributor.quantityAvailable -= quantity;

                // Persist the changes
                entityManager.merge(productDistributor);
            }

            // If all purchase requests are successfully processed, return true
            return true;
        } catch (NoResultException e) {
            System.out.println("Problem Purchase Service");
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
