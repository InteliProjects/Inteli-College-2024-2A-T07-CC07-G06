package com.nsync.service.cep;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import com.nsync.models.PurchaseRequest;

/**
 * REST resource for operations related to CEP (Postal Code) queries.
 * <p>
 * Provides endpoints for calculating estimated delivery days based on a provided
 * CEP. The service is mock-based and intended for testing and development purposes.
 * </p>
 *
 * <p>
 * The resource exposes an API to estimate the delivery days for a given CEP (postal code).
 * It validates the CEP format and uses a mock service to generate random delivery estimates.
 * The estimated delivery days are returned in a response object.
 * </p>
 *
 * <p>
 * This resource is annotated with OpenAPI annotations to describe the API and its operations.
 * The CEP format validation ensures that only valid postal codes are processed.
 * </p>
 *
 * <p>
 * The API is primarily intended for use in development and testing environments where mock data
 * is sufficient for estimating delivery days.
 * </p>
 *
 * <p>
 * Author: mauroDasChagas
 * </p>
 */
@Path("/cep")
@Tag(name = "\uD83C\uDFD9\uFE0F\u200B CEP Resource", description = "Operations related to CEP queries")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CepResource {

    @Inject
    CepDeliveryEstimator cepDeliveryEstimator;

    /**
     * Calculates the estimated delivery days for the given CEP.
     * <p>
     * This endpoint returns the first and last estimated delivery days based on
     * the provided CEP. The CEP must be in the format "12345-678" or "12345678".
     * If the format is invalid, an error response is returned.
     * </p>
     *
     * <p>
     * The calculation is performed using a mock service that generates random delivery estimates.
     * This method returns a 200 OK response with the delivery estimate if the CEP is valid,
     * or a 400 Bad Request response if the CEP format is invalid.
     * </p>
     *
     * @param cepRequest the request object containing the postal code (CEP) and product ID.
     * @return a {@code Response} containing the estimated delivery days if the request is valid,
     *         or an error message if the CEP format is invalid.
     */
    @POST
    @Path("/calculate-days")
    @Operation(summary = "Calculate delivery days for a given CEP", description = "Returns the first and last estimated delivery days based on the provided CEP.")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Calculation successful."),
            @APIResponse(responseCode = "400", description = "Invalid CEP provided.")
    })
    public Response calculateDays(CepRequest cepRequest) {
        String cep = cepRequest.getCep();
        if (cep == null || cep.isEmpty() || !cep.matches("\\d{5}-?\\d{3}")) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Invalid CEP format. Expected format: 12345-678 or 12345678.")
                    .build();
        }

        Long productId = cepRequest.getProductId();

        CepResponse response = cepDeliveryEstimator.calculateDays(cep, productId);
        return Response.ok(response).build();
    }

    @POST
    @Path("/calculate-days-purchase")
    @Operation(summary = "Calculate delivery days for a purchase", description = "Returns the estimated delivery days for each product in the purchase request.")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Calculation successful."),
            @APIResponse(responseCode = "400", description = "Invalid request.")
    })
    public Response calculateDaysForPurchase(PurchaseRequest purchaseRequest) {
        if (purchaseRequest == null || purchaseRequest.getProducts() == null || purchaseRequest.getProducts().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Invalid purchase request. No products provided.")
                    .build();
        }

        List<CepPurchaseResponse> responses = cepDeliveryEstimator.calculateDaysPurchase(purchaseRequest);
        ResponseWrapper<CepPurchaseResponse> responseWrapper = new ResponseWrapper<>(responses);
        return Response.ok(responseWrapper).build();
    }
}
